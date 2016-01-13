

-- Waypoint
INSERT INTO waypoint 
(ID, nr_of_satellites,record_time,position,track_id) 
VALUES ('86E28854-42FB-4CB4-A900-1A27852E3FAF',0,'2015-12-16',
point(-71.060316 ,48.432044)::geometry, '76e28854-42fb-4cb4-a900-1a27852e3faf' );

-- Track
INSERT INTO track ( id , start_date, device_id ) 
VALUES ( '76E28854-42FB-4CB4-A900-1A27852E3FAF' ,'2015-12-17 12:00:00', '01234567-42fb-4cb4-a900-765432103fa5' );
