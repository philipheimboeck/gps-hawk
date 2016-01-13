-- Analyse trackid from waypoints
SELECT trackid, deviceid , DATE_FORMAT(FROM_UNIXTIME(tbl_waypoint.timestampCaptured),'%Y-%m-%d') AS dat , COUNT(DISTINCT tbl_waypoint.id)  
FROM tbl_waypoint 
WHERE deviceid = '886d9fedcf62b929'
GROUP BY  trackid, deviceid , dat ORDER BY dat , deviceid, trackid;

SELECT COUNT(*) FROM tbl_waypoint;
SELECT COUNT(*) FROM tbl_motions;
SELECT COUNT(*) FROM tbl_track;

SELECT trackid, deviceid , FROM_UNIXTIME(tbl_waypoint.timestampCaptured) AS dat , lat , lng , provider , speed, altitude FROM tbl_waypoint WHERE deviceid = 'a43a994e03a607b1'; 

SELECT deviceid FROM tbl_waypoint GROUP BY deviceid ORDER BY deviceid;