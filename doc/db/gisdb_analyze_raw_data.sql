-- SELECT * FROM raw_motion_value ORDER BY timestamp DESC LIMIT 100;

-- SELECT external_id , to_timestamp(raw_motion_value.timestamp/1000) FROM raw_motion_value ORDER BY timestamp DESC LIMIT 10;

SELECT external_id , track_id , track.id, device.device_id, to_timestamp(timestamp/1000) 
FROM raw_waypoint 
INNER JOIN track ON raw_waypoint.track_id = track.id
INNER JOIN device ON device.id = track.device_id
ORDER BY timestamp DESC LIMIT 10;