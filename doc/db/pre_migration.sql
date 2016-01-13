-- script to run before creating migration-script (data cleaning!)
UPDATE tbl_waypoint SET timestampCaptured_ = NULL WHERE timestampCaptured_ = '0000-00-00 00:00:00';

UPDATE tbl_track SET isValid = 1 WHERE id = 295;

SELECT id, exportAt, Msg, Level, deviceid, dateTime , FROM_UNIXTIME(dateTime) FROM tbl_log_fhv WHERE tbl_log_fhv.Msg LIKE '%Is Valid: true' LIMIT 1000;

-- Delete null-values
DELETE FROM tbl_waypoint where trackid IS NULL;
UPDATE tbl_waypoint SET timestampCaptured = UNIX_TIMESTAMP(timestampCaptured_) WHERE timestampCaptured IS NULL AND timestampCaptured_ IS NOT NULL;
DELETE FROM tbl_waypoint WHERE timestampCaptured IS NULL;
