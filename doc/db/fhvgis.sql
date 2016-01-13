-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;
/*
ALTER TABLE ONLY public.waypoint DROP CONSTRAINT fk_waypoint_transportation_route_id;
ALTER TABLE ONLY public.waypoint DROP CONSTRAINT fk_waypoint_transportation_id;
ALTER TABLE ONLY public.waypoint DROP CONSTRAINT fk_waypoint_track_id;
ALTER TABLE ONLY public.waypoint DROP CONSTRAINT fk_waypoint_poi_id;
ALTER TABLE ONLY public.transportation_route DROP CONSTRAINT fk_transportation_route_transportation_id;
ALTER TABLE ONLY public.track DROP CONSTRAINT fk_track_device_id;
ALTER TABLE ONLY public.shedule DROP CONSTRAINT fk_shedule_transportation_route_id;
ALTER TABLE ONLY public.shedule DROP CONSTRAINT fk_shedule_shedule_day_id;
ALTER TABLE ONLY public.shedule DROP CONSTRAINT fk_shedule_poi_id;
ALTER TABLE ONLY public.segment DROP CONSTRAINT fk_segment_waypoint_to_id;
ALTER TABLE ONLY public.segment DROP CONSTRAINT fk_segment_waypoint_from_id;
ALTER TABLE ONLY public.segment DROP CONSTRAINT fk_segment_transportation_route_id;
ALTER TABLE ONLY public.segment DROP CONSTRAINT fk_segment_transportation_id;
ALTER TABLE ONLY public.segment DROP CONSTRAINT fk_segment_poi_to_id;
ALTER TABLE ONLY public.segment DROP CONSTRAINT fk_segment_poi_from_id;
ALTER TABLE ONLY public.poi DROP CONSTRAINT fk_poi_poi_type_id;
ALTER TABLE ONLY public.motion_values DROP CONSTRAINT fk_motion_value_device_id;
ALTER TABLE ONLY public.log DROP CONSTRAINT fk_log_device_id;
ALTER TABLE ONLY public.geofence DROP CONSTRAINT fk_geofence_poi_id;
ALTER TABLE ONLY public.device DROP CONSTRAINT fk_device_user_id;
ALTER TABLE ONLY public.waypoint DROP CONSTRAINT pk_waypoint_id;
ALTER TABLE ONLY public."user" DROP CONSTRAINT pk_user_id;
ALTER TABLE ONLY public.transportation DROP CONSTRAINT pk_transportition_id;
ALTER TABLE ONLY public.transportation_route DROP CONSTRAINT pk_transportation_route_id;
ALTER TABLE ONLY public.track DROP CONSTRAINT pk_track_id;
ALTER TABLE ONLY public.shedule DROP CONSTRAINT pk_shedule_id;
ALTER TABLE ONLY public.shedule_day DROP CONSTRAINT pk_shedule_day_id;
ALTER TABLE ONLY public.segment DROP CONSTRAINT pk_segment_id;
ALTER TABLE ONLY public.poi_type DROP CONSTRAINT pk_poi_type_id;
ALTER TABLE ONLY public.poi DROP CONSTRAINT pk_poi_id;
ALTER TABLE ONLY public.motion_values DROP CONSTRAINT pk_motion_value_id;
ALTER TABLE ONLY public.log DROP CONSTRAINT pk_log_id;
ALTER TABLE ONLY public.geofence DROP CONSTRAINT pk_geofence_id;
ALTER TABLE ONLY public.device DROP CONSTRAINT pk_device_id;
SET search_path = topology, pg_catalog;

SET search_path = public, pg_catalog;


DROP TABLE public.waypoint;
DROP TABLE public."user";
DROP TABLE public.transportation_route;
DROP TABLE public.transportation;
DROP TABLE public.track;
DROP TABLE public.shedule_day;
DROP TABLE public.shedule;
DROP TABLE public.segment;
DROP TABLE public.poi_type;
DROP TABLE public.poi;
DROP TABLE public.motion_values;
DROP TABLE public.log;
DROP TABLE public.geofence;
DROP TABLE public.device;
DROP EXTENSION "uuid-ossp";
DROP EXTENSION postgis_topology;
DROP EXTENSION postgis;
DROP EXTENSION adminpack;
DROP EXTENSION plpgsql;
DROP SCHEMA topology;
DROP SCHEMA public;
*/

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: topology; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA topology;


ALTER SCHEMA topology OWNER TO postgres;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- Name: postgis_topology; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS postgis_topology WITH SCHEMA topology;


--
-- Name: EXTENSION postgis_topology; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION postgis_topology IS 'PostGIS topology spatial types and functions';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

CREATE TABLE device (
    id uuid NOT NULL,
    token text NOT NULL,
    user_id uuid NOT NULL,
    device_id text
);


ALTER TABLE public.device OWNER TO gisuser;

--
-- Name: geofence; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE geofence (
    id uuid NOT NULL,
    poi_id uuid NOT NULL,
    positions geometry[] NOT NULL
);


ALTER TABLE public.geofence OWNER TO gisuser;

--
-- Name: log; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE log (
    id uuid NOT NULL,
    level text,
    log_time timestamp without time zone,
    tag text,
    message text,
    stacktrace text,
    device_id uuid NOT NULL
);


ALTER TABLE public.log OWNER TO gisuser;

--
-- Name: motion_values; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE motion_values (
    id uuid NOT NULL,
    device_id uuid NOT NULL,
    x double precision,
    y double precision,
    z double precision,
    motion_type integer,
    is_exported integer,
    date_time_captured bigint
);


ALTER TABLE public.motion_values OWNER TO gisuser;

--
-- Name: poi; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE poi (
    id uuid NOT NULL,
    name text,
    poi_type_id uuid NOT NULL,
    radius real,
    ext_ref text,
    "position" geometry NOT NULL
);


ALTER TABLE public.poi OWNER TO gisuser;

--
-- Name: poi_type; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE poi_type (
    id uuid NOT NULL,
    name text,
    private boolean DEFAULT false NOT NULL
);


ALTER TABLE public.poi_type OWNER TO gisuser;

--
-- Name: segment; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE segment (
    id uuid NOT NULL,
    transportation_analyzed_id uuid NOT NULL,
    transportation_route_analyzed_id uuid,
    waypoint_from_id uuid NOT NULL,
    waypoint_to_id uuid NOT NULL,
    poi_from_id uuid,
    poi_to_id uuid
);


ALTER TABLE public.segment OWNER TO gisuser;

--
-- Name: shedule; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE shedule (
    id uuid NOT NULL,
    trip_nr integer NOT NULL,
    valid_from timestamp without time zone,
    valid_until timestamp without time zone,
    explicit_date timestamp without time zone,
    arrival_time time without time zone NOT NULL,
    departure_time time without time zone,
    transportation_route_id uuid NOT NULL,
    shedule_day_id uuid NOT NULL,
    poi_id uuid NOT NULL,
    seq_no integer,
    CONSTRAINT check_shedule_trip_nr CHECK ((trip_nr >= 0))
);


ALTER TABLE public.shedule OWNER TO gisuser;

--
-- Name: shedule_day; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE shedule_day (
    id uuid NOT NULL,
    name text,
    description text
);


ALTER TABLE public.shedule_day OWNER TO gisuser;

--
-- Name: track; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE track (
    id uuid NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone,
    device_id uuid NOT NULL
);


ALTER TABLE public.track OWNER TO gisuser;

--
-- Name: transportation; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE transportation (
    id uuid NOT NULL,
    name text,
    max_speed real,
    avarage_speed real,
    color text,
    CONSTRAINT check_transportation_avarage_speed CHECK ((avarage_speed >= (0)::double precision)),
    CONSTRAINT check_transportation_max_speed CHECK ((max_speed >= (0)::double precision))
);


ALTER TABLE public.transportation OWNER TO gisuser;

--
-- Name: transportation_route; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE transportation_route (
    id uuid NOT NULL,
    name text,
    valid_from timestamp without time zone NOT NULL,
    valid_until timestamp without time zone,
    transportation_id uuid NOT NULL,
    operator text,
    network text,
    ext_ref text,
    desc_from text,
    desc_to text,
    "desc" text,
    route_no text
);


ALTER TABLE public.transportation_route OWNER TO gisuser;

--
-- Name: user; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE "user" (
    id uuid NOT NULL,
    username text NOT NULL,
    password text NOT NULL
);


ALTER TABLE public."user" OWNER TO gisuser;

--
-- Name: waypoint; Type: TABLE; Schema: public; Owner: gisuser; Tablespace:
--

CREATE TABLE waypoint (
    id uuid NOT NULL,
    nr_of_satellites integer NOT NULL,
    record_time timestamp without time zone NOT NULL,
    accuracy double precision,
    transportation_superv_id uuid,
    transportation_route_superv_id uuid,
    poi_id uuid,
    valid boolean DEFAULT false NOT NULL,
    speed real,
    "position" geometry NOT NULL,
    track_id uuid NOT NULL,
    CONSTRAINT check_waypoint_nr_of_satellites CHECK ((nr_of_satellites >= 0))
);


ALTER TABLE public.waypoint OWNER TO gisuser;

SET search_path = public, pg_catalog;
--
-- Name: pk_device_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY device
    ADD CONSTRAINT pk_device_id PRIMARY KEY (id);


--
-- Name: pk_geofence_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY geofence
    ADD CONSTRAINT pk_geofence_id PRIMARY KEY (id);


--
-- Name: pk_log_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY log
    ADD CONSTRAINT pk_log_id PRIMARY KEY (id);


--
-- Name: pk_motion_value_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY motion_values
    ADD CONSTRAINT pk_motion_value_id PRIMARY KEY (id);


--
-- Name: pk_poi_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY poi
    ADD CONSTRAINT pk_poi_id PRIMARY KEY (id);


--
-- Name: pk_poi_type_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY poi_type
    ADD CONSTRAINT pk_poi_type_id PRIMARY KEY (id);


--
-- Name: pk_segment_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT pk_segment_id PRIMARY KEY (id);


--
-- Name: pk_shedule_day_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY shedule_day
    ADD CONSTRAINT pk_shedule_day_id PRIMARY KEY (id);


--
-- Name: pk_shedule_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY shedule
    ADD CONSTRAINT pk_shedule_id PRIMARY KEY (id);


--
-- Name: pk_track_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY track
    ADD CONSTRAINT pk_track_id PRIMARY KEY (id);


--
-- Name: pk_transportation_route_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY transportation_route
    ADD CONSTRAINT pk_transportation_route_id PRIMARY KEY (id);


--
-- Name: pk_transportition_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY transportation
    ADD CONSTRAINT pk_transportition_id PRIMARY KEY (id);


--
-- Name: pk_user_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT pk_user_id PRIMARY KEY (id);


--
-- Name: pk_waypoint_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace:
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT pk_waypoint_id PRIMARY KEY (id);


--
-- Name: fk_device_user_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fk_device_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_geofence_poi_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY geofence
    ADD CONSTRAINT fk_geofence_poi_id FOREIGN KEY (poi_id) REFERENCES poi(id);


--
-- Name: fk_log_device_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY log
    ADD CONSTRAINT fk_log_device_id FOREIGN KEY (device_id) REFERENCES device(id);


--
-- Name: fk_motion_value_device_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY motion_values
    ADD CONSTRAINT fk_motion_value_device_id FOREIGN KEY (device_id) REFERENCES device(id);


--
-- Name: fk_poi_poi_type_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY poi
    ADD CONSTRAINT fk_poi_poi_type_id FOREIGN KEY (poi_type_id) REFERENCES poi_type(id);


--
-- Name: fk_segment_poi_from_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_poi_from_id FOREIGN KEY (poi_from_id) REFERENCES poi(id);


--
-- Name: fk_segment_poi_to_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_poi_to_id FOREIGN KEY (poi_to_id) REFERENCES poi(id);


--
-- Name: fk_segment_transportation_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_transportation_id FOREIGN KEY (transportation_analyzed_id) REFERENCES transportation(id);


--
-- Name: fk_segment_transportation_route_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_transportation_route_id FOREIGN KEY (transportation_route_analyzed_id) REFERENCES transportation_route(id);


--
-- Name: fk_segment_waypoint_from_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_waypoint_from_id FOREIGN KEY (waypoint_from_id) REFERENCES waypoint(id);


--
-- Name: fk_segment_waypoint_to_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_waypoint_to_id FOREIGN KEY (waypoint_to_id) REFERENCES waypoint(id);


--
-- Name: fk_shedule_poi_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY shedule
    ADD CONSTRAINT fk_shedule_poi_id FOREIGN KEY (poi_id) REFERENCES poi(id);


--
-- Name: fk_shedule_shedule_day_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY shedule
    ADD CONSTRAINT fk_shedule_shedule_day_id FOREIGN KEY (shedule_day_id) REFERENCES shedule_day(id);


--
-- Name: fk_shedule_transportation_route_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY shedule
    ADD CONSTRAINT fk_shedule_transportation_route_id FOREIGN KEY (transportation_route_id) REFERENCES transportation_route(id);


--
-- Name: fk_track_device_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY track
    ADD CONSTRAINT fk_track_device_id FOREIGN KEY (device_id) REFERENCES device(id);


--
-- Name: fk_transportation_route_transportation_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY transportation_route
    ADD CONSTRAINT fk_transportation_route_transportation_id FOREIGN KEY (transportation_id) REFERENCES transportation(id);


--
-- Name: fk_waypoint_poi_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT fk_waypoint_poi_id FOREIGN KEY (poi_id) REFERENCES poi(id);


--
-- Name: fk_waypoint_track_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT fk_waypoint_track_id FOREIGN KEY (track_id) REFERENCES track(id);


--
-- Name: fk_waypoint_transportation_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT fk_waypoint_transportation_id FOREIGN KEY (transportation_superv_id) REFERENCES transportation(id);


--
-- Name: fk_waypoint_transportation_route_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT fk_waypoint_transportation_route_id FOREIGN KEY (transportation_route_superv_id) REFERENCES transportation_route(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;

SELECT * FROM pg_catalog.pg_tables
