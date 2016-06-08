Test with new MapEntity - seems that KeyMapEntity adds little value.
But MapEntity is much better than Map - without optimising equal.

11:23:35>1)CQEngine Test - using threaded.car.Car objects
11:23:35>cars created = 20000
11:23:41>elapsed = 6272.9587ms for 20000 queries. Time per query:0.3136ms

11:23:41>1)CQEngine Test - using threaded.car.Car objects/nullable attribs
11:23:41>cars created = 20000
11:23:47>elapsed = 6082.9562ms for 20000 queries. Time per query:0.3041ms

11:23:47>1)CQEngine Test - using Map objects
11:23:47>cars created = 20000
11:24:00>elapsed = 13103.5492ms for 20000 queries. Time per query:0.6552ms

11:24:00>1)CQEngine Test - using Map objects with nullable attribs
11:24:00>cars created = 20000
11:24:13>elapsed = 12980.0660ms for 20000 queries. Time per query:0.6490ms

11:24:13>1)CQEngine Test - using Map objects and MapAttribs
11:24:13>cars created = 20000
11:24:26>elapsed = 13411.4931ms for 20000 queries. Time per query:0.6706ms

11:24:26>1)CQEngine Test - using MapEntity objects and MapEntityAttribs
11:24:27>cars created = 20000
11:24:34>elapsed = 7081.2452ms for 20000 queries. Time per query:0.3541ms

11:24:34>1)CQEngine Test - using KeyedMapEntity objects and MapEntityAttribs
11:24:34>cars created = 20000
11:24:40>elapsed = 6842.9486ms for 20000 queries. Time per query:0.3421ms


---

Test 3 - more tests, using latest map attrib code - v.slow with MapAttribs

CQEngine perf.Test2 - lots of queries - starting
CQEngine Test - using threaded.car.Car objects
cars created = 10000
elapsed = 4182.4028ms for 10000 queries. Time per query:0.4182ms

CQEngine Test - using threaded.car.Car objects/nullable attribs
cars created = 10000
elapsed = 4135.2624ms for 10000 queries. Time per query:0.4135ms

CQEngine Test - using Map objects
cars created = 10000
elapsed = 7674.7680ms for 10000 queries. Time per query:0.7675ms

CQEngine Test - using Map objects with nullable attribs
cars created = 10000
elapsed = 6922.3746ms for 10000 queries. Time per query:0.6922ms

CQEngine Test - using Map objects and MapAttribs
cars created = 10000
elapsed = 20330.4781ms for 10000 queries. Time per query:2.0330ms

CQEngine Test - done


Test 2 - not too many entries, but lots of queries
CQEngine perf.Test2 - lots of queries - starting
CQEngine Test - using threaded.car.Car objects
cars created = 10000
elapsed = 28057.6352ms for 100000 queries. Time per query:0.2806ms
CQEngine Test - using Map objects
cars created = 10000
elapsed = 44376.5772ms for 100000 queries. Time per query:0.4438ms
CQEngine Test - done


10 seconds, dedup, slow counter

CQEngine Test - starting
cars created = 3152792
elapsed = 91043.7747ms for 315 queries. Time per query:289.0279ms
cars created = 3152792
elapsed = 175876.0640ms for 315 queries. Time per query:558.3367ms
CQEngine Test - done


With seconds of 2, manually counting results and dedup option

CQEngine Test - starting
cars created = 869759
elapsed = 6238.3678ms for 86 queries. Time per query:72.5392ms
cars created = 869759
elapsed = 10456.3116ms for 86 queries. Time per query:121.5850ms
CQEngine Test - done

With seconds of 2, using size count option
CQEngine Test - starting
cars created = 813619
elapsed = 5888.2645ms for 81 queries. Time per query:72.6946ms
cars created = 813619
elapsed = 9492.9913ms for 81 queries. Time per query:117.1974ms
CQEngine Test - done


With secondsForCreation of 1

CQEngine Test - starting
cars created = 180600
elapsed = 239.1547ms for 18060 queries. Time per query:0.0132ms
cars created = 180600
elapsed = 103.0784ms for 18060 queries. Time per query:0.0057ms
CQEngine Test - done

For 10 seconds

CQEngine Test - starting
cars created = 1715055
elapsed = 619.0487ms for 171505 queries. Time per query:0.0036ms
cars created = 1715055
elapsed = 595.7414ms for 171505 queries. Time per query:0.0035ms
CQEngine Test - done

