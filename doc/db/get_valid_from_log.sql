-- Get log time
SELECT id, exportAt, Msg, Level, deviceid, dateTime , FROM_UNIXTIME(dateTime) FROM tbl_log_fhv WHERE tbl_log_fhv.Msg LIKE '%Is Valid: true' LIMIT 1000;

SELECT w.trackid, COUNT(*) , GROUP_CONCAT(DISTINCT w.deviceid)
FROM tbl_log_fhv AS g 
LEFT JOIN tbl_waypoint AS w ON w.timestampCaptured <= g.dateTime AND w.timestampCaptured > ( g.dateTime -100 ) AND g.deviceid = w.deviceid
WHERE g.Msg LIKE '%Is Valid: true'
GROUP BY w.trackid;

-- Get waypoints at this time
SELECT id , speed , datetime , provider,deviceid,trackid,timestampCaptured,FROM_UNIXTIME(timestampCaptured) FROM tbl_waypoint 
WHERE ABS( 1450422399 - timestampCaptured) < 100 AND deviceid = '886d9fedcf62b929' LIMIT 10;


-- more readable test - Stichproben
SELECT w.id , w.provider,w.deviceid,w.trackid,w.timestampCaptured,FROM_UNIXTIME(w.timestampCaptured) AS wp_time
FROM tbl_waypoint AS w
WHERE w.timestampCaptured <= 1450770045 AND w.timestampCaptured > ( 1450770045 -100 ) ORDER BY w.timestampCaptured DESC LIMIT 1; 

SELECT w.id , w.provider,w.deviceid,w.trackid,w.timestampCaptured,FROM_UNIXTIME(w.timestampCaptured) AS wp_time
FROM tbl_waypoint AS w
WHERE w.timestampCaptured <= 1450712074 AND w.timestampCaptured > ( 1450712074 -100 ) ORDER BY w.timestampCaptured DESC LIMIT 1; 

SELECT w.id , w.provider,w.deviceid,w.trackid,w.timestampCaptured,FROM_UNIXTIME(w.timestampCaptured) AS wp_time
FROM tbl_waypoint AS w
WHERE w.timestampCaptured <= 1450678502 AND w.timestampCaptured > ( 1450678502 -100 ) ORDER BY w.timestampCaptured DESC LIMIT 1; 

SELECT w.id , w.provider,w.deviceid,w.trackid,w.timestampCaptured,FROM_UNIXTIME(w.timestampCaptured) AS wp_time
FROM tbl_waypoint AS w
WHERE w.timestampCaptured <= 1450421484 AND w.timestampCaptured > ( 1450421484 -100 ) ORDER BY w.timestampCaptured DESC LIMIT 1; 


-- THE UPDATE!!!
-- UPDATE tbl_track SET isValid = 1 WHERE id IN (4,5,7,8,9,144,145,147,148,149,150,151,152,153,154,155,163,164,165,166,167,186,187,188,189,192,193,194,202,204,205,207,209,221,222,223,224,225,226,229,230,238,239,245,248,252,253,254,256,259,260,263,264,266,272,273,274,276,279,280,281,282,283,284,285,286,290,294,296,297,298,300,301,302,303,304,305,310,312,313,314,315,316,317,320,323,34,325,348,358,360) AND isValid = 0;