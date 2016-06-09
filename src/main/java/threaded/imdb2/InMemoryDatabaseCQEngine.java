package threaded.imdb2;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection2;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.AttributeOrder;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Created by kimptonc on 09/09/2014.
 */
public class InMemoryDatabaseCQEngine {

    private final MyLogger log = new MyLogger();

    private Map<String, TableConfigCQE> tableConfigMap = new HashMap<String, TableConfigCQE>();

    public List<Map<String, Object>> queryByField(String table, String field, String... values) {
        return queryByFieldSorted(table, field, values);
    }

    public List<Map<String, Object>> queryByFieldSorted(String table, String field, String[] values, String... sortFields) {
        TableConfigCQE config = getTableConfig(table);
        QueryOptions queryOptions = QueryFactory.noQueryOptions();
        if (sortFields != null && sortFields.length > 0) {
            List<AttributeOrder<Map>> sortOrder = new ArrayList<>();
            for (String sortField : sortFields) {
                if (sortField != null) sortOrder.add(ascending(config.getAttribute(sortField)));
            }
            if (sortOrder.size() > 0) queryOptions = queryOptions(orderBy(sortOrder));
        }
        Collection valuesWithoutNulls = new ArrayList();
        for (String value : values) {
            if (value != null) valuesWithoutNulls.add(value);
        }
        ResultSet<Map> set = null;
        try {
            set = config.queryCollection(
                    QueryFactory.in(config.getAttribute(field), valuesWithoutNulls),
                    queryOptions);
            return resultSetToList(set);
        } finally {
            if (set != null) set.close();
        }
    }

    private List<Map<String, Object>> resultSetToList(ResultSet<Map> set) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map map : set) {
            result.add(map);
        }
        return result;
    }

    public int size(String table) {
        return getIndexedCollection(table).size();
    }

    public boolean put(String table, String key, Map<String, Object> values) {
        // could be a new record or an update
        TableConfigCQE config = getTableConfig(table);
        if (config == null) {
            log.error("Table not initialised:"+table);
            return false;
        }
        if (values.get(config.getPrimaryKey()) == null) {
            StringBuilder keysFound = new StringBuilder();
            for (String keyStr : values.keySet())
            {
                if (keysFound.length() > 0) keysFound.append(",");
                keysFound.append(keyStr);
            }
            log.error("No key field ("+table+"/"+key+"/"+config.getPrimaryKey()+") found in supplied keys ("+keysFound+")");
            return false;
        }
        if (key == null) {
            log.error("Key must not be null, table:"+table);
            return false;
        }
        ResultSet<Map> existingResults = null;
        Map<String, Object> existing;
        try {
            existingResults = config.queryCollection(equal(config.getPrimaryKeyAttrib(), key));
            if (existingResults != null && existingResults.size() > 1) {
                log.error("More than 1 records matches primary key:"+key+" on table "+table);
                return false;
            }
            existing = null;
            // might be a performance hit from removing/re-adding, but it does the job. Alternative is to do update, but then need to copy map and call modify
            if (existingResults != null && existingResults.size() == 1) {
                existing = existingResults.uniqueResult();
            }
        } finally {
            if (existingResults != null) existingResults.close();
        }
        return config.put(existing, values);
    }

    private IndexedCollection<Map> getIndexedCollection(String name) {
        TableConfigCQE config = tableConfigMap.get(name);
        if (config == null) {
            log.info("Trying to access table:"+name+" but its not been initialised!");
            return null;
        }
        IndexedCollection<Map> repo = config.getIndexedCollection();
        if (repo == null) {
            log.warn("Trying to access table:"+name+" we have config, but no indexed collection!");
        }
        return repo;
    }

    private TableConfigCQE getTableConfig(String name) {
        TableConfigCQE config = tableConfigMap.get(name);
        if (config == null) {
            log.info("Trying to access table:" + name + " but its not been initialised!");
            return null;
        }
        return config;
    }

    public void initTable(String table, String primaryKey, String... indexFields) {
        synchronized (tableConfigMap) {
            if (!tableConfigMap.containsKey(table)) {
                log.info("InMemoryDatabase:initTable called for "+table+"("+primaryKey+")");
                tableConfigMap.put(table, new TableConfigCQE(primaryKey, indexFields));
            } else {
                log.warn("InMemoryDatabase:Cache Table "+table+" has been initialised already!");
            }
        }
    }

    private class MyLogger {
        public void error(String s) {
            System.out.println(s);
        }

        public void info(String s) {
            System.out.println(s);
        }
        public void warn(String s) {
            System.out.println(s);
        }
    }
}

class TableConfigCQE {
    private static final String IN_MEMORY_DB_VERSION = "IN_MEMORY_DB_VERSION";
    private IndexedCollection indexedCollection;
    private String primaryKey; // default insert key
    private Set<String> lookupIndexes = new HashSet<>();
    private Map<String, Attribute> attributes = new HashMap<>();
    private final AtomicLong versionGenerator = new AtomicLong();

    TableConfigCQE(String primaryKey, String[] indexes) {
        this.primaryKey = primaryKey;
        if (indexes != null) addLookupIndexes(indexes);
    }

    void addLookupIndexes(String... indexes)
    {
        for (String index : indexes) {
            if (index != null) // ignore if null
                lookupIndexes.add(index);
        }
    }

    IndexedCollection<Map> getIndexedCollection() {
        if (indexedCollection == null) {
            synchronized (this) {
                if (indexedCollection == null) {
                    // ConcIndexColl has the issue
                    indexedCollection = new ConcurrentIndexedCollection<>();
                    // latest TransIndeColl is fine
//                    indexedCollection = new TransactionalIndexedCollection<>(Map.class);
//                    indexedCollection = new TransactionalIndexedCollection2<>(Map.class);
                    // hash index gives intermittently empty resultSet
                    indexedCollection.addIndex(HashIndex.onAttribute(getAttribute(primaryKey)));
                    // unique index works fine
//                    indexedCollection.addIndex(UniqueIndex.onAttribute(getAttribute(primaryKey)));
                    for (String index : lookupIndexes) {
                        Attribute<Map, Comparable> attribute = getAttribute(index);
                        if (!attribute.equals(primaryKey)) indexedCollection.addIndex(HashIndex.onAttribute(attribute));
                    }
                }
            }
        }
        return indexedCollection;
    }

    ResultSet<Map> queryCollection(Query<Map> query)
    {
        return queryCollection(query, noQueryOptions());
    }

    synchronized ResultSet<Map> queryCollection(Query<Map> query, QueryOptions options)
    {
        return getIndexedCollection().retrieve(query, options);
    }

    synchronized boolean put(Map oldMap, Map newMap)
    {
        boolean added_ok = false;
        if (oldMap != null) {
            Map newMergedMap = new HashMap(oldMap);
            newMergedMap.putAll(newMap); // merge key/value fields
            newMergedMap.put(IN_MEMORY_DB_VERSION, versionGenerator.incrementAndGet()); // required to enable the indexed collection to handle update
            added_ok = getIndexedCollection().update(Collections.singleton(oldMap), Collections.singleton(newMergedMap));
        } else {
            added_ok = getIndexedCollection().add(newMap);
        }
        return added_ok;
    }

    Attribute getPrimaryKeyAttrib()
    {
        return getAttribute(primaryKey);
    }

    Attribute<Map, Comparable> getAttribute(final String key) {

        Attribute<Map, Comparable> attribute = attributes.get(key);
        if (attribute == null) {
            attribute = new SimpleNullableAttribute<Map, Comparable>(key) {
                @Override
                public Comparable getValue(Map o, QueryOptions queryOptions) {
                    Object value = o.get(key);
                    if (value == null) value = "";
                    return (Comparable) value;
                }
            };
            attributes.put(key, attribute);
        }
        return attribute;
    }

    String getPrimaryKey() {
        return primaryKey;
    }
}