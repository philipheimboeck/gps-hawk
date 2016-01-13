
-- SELECT those
-- and then delete all where id in GROUP_CONCAT-column >= 2
SELECT  trackid , provider , bearing, deviceid , timestampCaptured , COUNT(*) c , GROUP_CONCAT(id ORDER BY id ASC SEPARATOR ', ' )
FROM tbl_waypoint 
GROUP BY trackid , provider , bearing, deviceid , timestampCaptured 
HAVING c > 1;