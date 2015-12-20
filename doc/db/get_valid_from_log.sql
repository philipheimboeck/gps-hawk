-- Get log time
SELECT id, exportAt, Msg, Level, deviceid, dateTime , FROM_UNIXTIME(dateTime) FROM tbl_log_fhv WHERE tbl_log_fhv.Msg LIKE '%Is Valid: true' LIMIT 1000;

-- Get waypoints at this time
SELECT id , speed , datetime , provider,deviceid,trackid,timestampCaptured,FROM_UNIXTIME(timestampCaptured) FROM tbl_waypoint 
WHERE ABS( 1450422399 - timestampCaptured) < 100 AND deviceid = '886d9fedcf62b929' LIMIT 10;


-- more readable test
SELECT id , speed , datetime , provider,deviceid,trackid,timestampCaptured,FROM_UNIXTIME(timestampCaptured) 
FROM tbl_waypoint 
WHERE FROM_UNIXTIME(timestampCaptured) < '2015-12-18 08:06:39' AND FROM_UNIXTIME(timestampCaptured) > '2015-12-18 07:06:39' AND deviceid = '886d9fedcf62b929'; 