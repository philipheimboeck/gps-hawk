--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.10
-- Dumped by pg_dump version 9.3.10
-- Started on 2015-12-17 10:20:46 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 7 (class 2615 OID 17672)
-- Name: topology; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA topology;


ALTER SCHEMA topology OWNER TO postgres;

--
-- TOC entry 207 (class 3079 OID 11787)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 207
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 206 (class 3079 OID 18033)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 206
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- TOC entry 209 (class 3079 OID 16386)
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 209
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- TOC entry 210 (class 3079 OID 17673)
-- Name: postgis_topology; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis_topology WITH SCHEMA topology;


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 210
-- Name: EXTENSION postgis_topology; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis_topology IS 'PostGIS topology spatial types and functions';


--
-- TOC entry 208 (class 3079 OID 18043)
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 208
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 191 (class 1259 OID 17840)
-- Name: device; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE device (
    id uuid NOT NULL,
    token text NOT NULL,
    user_id uuid NOT NULL,
    device_id text
);


ALTER TABLE public.device OWNER TO gisuser;

--
-- TOC entry 196 (class 1259 OID 17893)
-- Name: geofence; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE geofence (
    id uuid NOT NULL,
    poi_id uuid NOT NULL,
    positions geometry[] NOT NULL,
    center geometry,
    radius double precision
);


ALTER TABLE public.geofence OWNER TO gisuser;

--
-- TOC entry 202 (class 1259 OID 18067)
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
-- TOC entry 203 (class 1259 OID 18088)
-- Name: motion_values; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE motion_values (
    id uuid NOT NULL,
    device_id uuid NOT NULL,
    x double precision,
    y double precision,
    z double precision,
    motion_type integer,
    "timestamp" timestamp without time zone,
    is_exported boolean
);


ALTER TABLE public.motion_values OWNER TO gisuser;

--
-- TOC entry 195 (class 1259 OID 17880)
-- Name: poi; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE poi (
    id uuid NOT NULL,
    name text,
    poitype_id uuid NOT NULL,
    radius real,
    ext_ref text,
    "position" geometry NOT NULL
);


ALTER TABLE public.poi OWNER TO gisuser;

--
-- TOC entry 194 (class 1259 OID 17871)
-- Name: poi_type; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE poi_type (
    id uuid NOT NULL,
    name text,
    private boolean DEFAULT false NOT NULL
);


ALTER TABLE public.poi_type OWNER TO gisuser;

--
-- TOC entry 204 (class 1259 OID 18103)
-- Name: raw_motion_value; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE raw_motion_value (
    id uuid NOT NULL,
    external_id integer,
    x double precision,
    y double precision,
    z double precision,
    motion_type integer,
    device_id uuid,
    "timestamp" bigint
);


ALTER TABLE public.raw_motion_value OWNER TO gisuser;

--
-- TOC entry 205 (class 1259 OID 18113)
-- Name: raw_waypoint; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE raw_waypoint (
    id uuid NOT NULL,
    external_id integer,
    nr_of_satellites integer,
    "timestamp" timestamp without time zone,
    accuracy double precision,
    speed double precision,
    bearing double precision,
    provider text,
    longitude double precision,
    latitude double precision,
    altitude double precision,
    transportation_id uuid,
    track_id uuid NOT NULL
);


ALTER TABLE public.raw_waypoint OWNER TO gisuser;

--
-- TOC entry 201 (class 1259 OID 17998)
-- Name: schedule; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE schedule (
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


ALTER TABLE public.schedule OWNER TO gisuser;

--
-- TOC entry 193 (class 1259 OID 17863)
-- Name: schedule_day; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE schedule_day (
    id uuid NOT NULL,
    name text,
    description text
);


ALTER TABLE public.schedule_day OWNER TO gisuser;

--
-- TOC entry 200 (class 1259 OID 17963)
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
-- TOC entry 192 (class 1259 OID 17853)
-- Name: track; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE track (
    id uuid NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone,
    device_id uuid
);


ALTER TABLE public.track OWNER TO gisuser;

--
-- TOC entry 197 (class 1259 OID 17914)
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
-- TOC entry 198 (class 1259 OID 17923)
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
    desce text,
    route_no text
);


ALTER TABLE public.transportation_route OWNER TO gisuser;

--
-- TOC entry 190 (class 1259 OID 17832)
-- Name: user; Type: TABLE; Schema: public; Owner: gisuser; Tablespace: 
--

CREATE TABLE "user" (
    id uuid NOT NULL,
    username text NOT NULL,
    password text NOT NULL
);


ALTER TABLE public."user" OWNER TO gisuser;

--
-- TOC entry 199 (class 1259 OID 17936)
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

--
-- TOC entry 3481 (class 0 OID 17840)
-- Dependencies: 191
-- Data for Name: device; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY device (id, token, user_id, device_id) FROM stdin;
6fbc57b9-f216-452a-8157-86752b13c5d9	nmea-tocken	8d3c1dca-ac8b-4e81-b13d-74947837ab46	NMEA
\.


--
-- TOC entry 3486 (class 0 OID 17893)
-- Dependencies: 196
-- Data for Name: geofence; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY geofence (id, poi_id, positions, center, radius) FROM stdin;
\.


--
-- TOC entry 3492 (class 0 OID 18067)
-- Dependencies: 202
-- Data for Name: log; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY log (id, level, log_time, tag, message, stacktrace, device_id) FROM stdin;
\.


--
-- TOC entry 3493 (class 0 OID 18088)
-- Dependencies: 203
-- Data for Name: motion_values; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY motion_values (id, device_id, x, y, z, motion_type, "timestamp", is_exported) FROM stdin;
\.


--
-- TOC entry 3485 (class 0 OID 17880)
-- Dependencies: 195
-- Data for Name: poi; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY poi (id, name, poitype_id, radius, ext_ref, "position") FROM stdin;
\.


--
-- TOC entry 3484 (class 0 OID 17871)
-- Dependencies: 194
-- Data for Name: poi_type; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY poi_type (id, name, private) FROM stdin;
8cad9e05-d7f0-4ffc-8ea8-b37352d7cae5	home	f
bd740afb-d7fd-4f00-ab0f-478139a02785	home	f
8d6585ab-db5e-4235-adcd-9403d3b35ef4	home	f
ed72d169-b5f4-485e-834a-b7405869d81d	home	f
0fa6cb8c-5a3b-400d-b6fa-5e161004e8a2	home	f
f4912c72-fc41-40f8-9132-07fb1bb5629f	home	f
3f6f6940-daef-4768-993f-b5312a65e177	home	f
\.


--
-- TOC entry 3494 (class 0 OID 18103)
-- Dependencies: 204
-- Data for Name: raw_motion_value; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY raw_motion_value (id, external_id, x, y, z, motion_type, device_id, "timestamp") FROM stdin;
\.


--
-- TOC entry 3495 (class 0 OID 18113)
-- Dependencies: 205
-- Data for Name: raw_waypoint; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY raw_waypoint (id, external_id, nr_of_satellites, "timestamp", accuracy, speed, bearing, provider, longitude, latitude, altitude, transportation_id, track_id) FROM stdin;
\.


--
-- TOC entry 3491 (class 0 OID 17998)
-- Dependencies: 201
-- Data for Name: schedule; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY schedule (id, trip_nr, valid_from, valid_until, explicit_date, arrival_time, departure_time, transportation_route_id, shedule_day_id, poi_id, seq_no) FROM stdin;
\.


--
-- TOC entry 3483 (class 0 OID 17863)
-- Dependencies: 193
-- Data for Name: schedule_day; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY schedule_day (id, name, description) FROM stdin;
4b8e0dd7-0ed6-4944-a9a2-261bb4526375	Werktags	Montag bis Freitag
c260e5e2-977e-4f36-9415-4f79681a4bef	Werktags	Montag bis Freitag
\.


--
-- TOC entry 3490 (class 0 OID 17963)
-- Dependencies: 200
-- Data for Name: segment; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY segment (id, transportation_analyzed_id, transportation_route_analyzed_id, waypoint_from_id, waypoint_to_id, poi_from_id, poi_to_id) FROM stdin;
\.


--
-- TOC entry 3298 (class 0 OID 16654)
-- Dependencies: 172
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- TOC entry 3482 (class 0 OID 17853)
-- Dependencies: 192
-- Data for Name: track; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY track (id, start_date, end_date, device_id) FROM stdin;
\.


--
-- TOC entry 3487 (class 0 OID 17914)
-- Dependencies: 197
-- Data for Name: transportation; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY transportation (id, name, max_speed, avarage_speed, color) FROM stdin;
\.


--
-- TOC entry 3488 (class 0 OID 17923)
-- Dependencies: 198
-- Data for Name: transportation_route; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY transportation_route (id, name, valid_from, valid_until, transportation_id, operator, network, ext_ref, desc_from, desc_to, desce, route_no) FROM stdin;
\.


--
-- TOC entry 3480 (class 0 OID 17832)
-- Dependencies: 190
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY "user" (id, username, password) FROM stdin;
8d3c1dca-ac8b-4e81-b13d-74947837ab46	NMEA	nmea
\.


--
-- TOC entry 3489 (class 0 OID 17936)
-- Dependencies: 199
-- Data for Name: waypoint; Type: TABLE DATA; Schema: public; Owner: gisuser
--

COPY waypoint (id, nr_of_satellites, record_time, accuracy, transportation_superv_id, transportation_route_superv_id, poi_id, valid, speed, "position", track_id) FROM stdin;
\.


SET search_path = topology, pg_catalog;

--
-- TOC entry 3299 (class 0 OID 17676)
-- Dependencies: 185
-- Data for Name: topology; Type: TABLE DATA; Schema: topology; Owner: postgres
--

COPY topology (id, name, srid, "precision", hasz) FROM stdin;
\.


--
-- TOC entry 3300 (class 0 OID 17689)
-- Dependencies: 186
-- Data for Name: layer; Type: TABLE DATA; Schema: topology; Owner: postgres
--

COPY layer (topology_id, layer_id, schema_name, table_name, feature_column, feature_type, level, child_id) FROM stdin;
\.


SET search_path = public, pg_catalog;

--
-- TOC entry 3314 (class 2606 OID 17847)
-- Name: pk_device_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY device
    ADD CONSTRAINT pk_device_id PRIMARY KEY (id);


--
-- TOC entry 3324 (class 2606 OID 17898)
-- Name: pk_geofence_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY geofence
    ADD CONSTRAINT pk_geofence_id PRIMARY KEY (id);


--
-- TOC entry 3336 (class 2606 OID 18074)
-- Name: pk_log_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY log
    ADD CONSTRAINT pk_log_id PRIMARY KEY (id);


--
-- TOC entry 3338 (class 2606 OID 18092)
-- Name: pk_motion_value_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY motion_values
    ADD CONSTRAINT pk_motion_value_id PRIMARY KEY (id);


--
-- TOC entry 3322 (class 2606 OID 17887)
-- Name: pk_poi_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY poi
    ADD CONSTRAINT pk_poi_id PRIMARY KEY (id);


--
-- TOC entry 3320 (class 2606 OID 17879)
-- Name: pk_poi_type_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY poi_type
    ADD CONSTRAINT pk_poi_type_id PRIMARY KEY (id);


--
-- TOC entry 3340 (class 2606 OID 18107)
-- Name: pk_raw_motion_value_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY raw_motion_value
    ADD CONSTRAINT pk_raw_motion_value_id PRIMARY KEY (id);


--
-- TOC entry 3342 (class 2606 OID 18117)
-- Name: pk_raw_waypoint_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY raw_waypoint
    ADD CONSTRAINT pk_raw_waypoint_id PRIMARY KEY (id);


--
-- TOC entry 3332 (class 2606 OID 17967)
-- Name: pk_segment_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT pk_segment_id PRIMARY KEY (id);


--
-- TOC entry 3318 (class 2606 OID 17870)
-- Name: pk_shedule_day_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY schedule_day
    ADD CONSTRAINT pk_shedule_day_id PRIMARY KEY (id);


--
-- TOC entry 3334 (class 2606 OID 18003)
-- Name: pk_shedule_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT pk_shedule_id PRIMARY KEY (id);


--
-- TOC entry 3316 (class 2606 OID 17857)
-- Name: pk_track_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY track
    ADD CONSTRAINT pk_track_id PRIMARY KEY (id);


--
-- TOC entry 3328 (class 2606 OID 17930)
-- Name: pk_transportation_route_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY transportation_route
    ADD CONSTRAINT pk_transportation_route_id PRIMARY KEY (id);


--
-- TOC entry 3326 (class 2606 OID 17922)
-- Name: pk_transportition_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY transportation
    ADD CONSTRAINT pk_transportition_id PRIMARY KEY (id);


--
-- TOC entry 3312 (class 2606 OID 17839)
-- Name: pk_user_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT pk_user_id PRIMARY KEY (id);


--
-- TOC entry 3330 (class 2606 OID 17942)
-- Name: pk_waypoint_id; Type: CONSTRAINT; Schema: public; Owner: gisuser; Tablespace: 
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT pk_waypoint_id PRIMARY KEY (id);


--
-- TOC entry 3363 (class 2606 OID 18108)
-- Name: fK_raw_motion_value_device_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY raw_motion_value
    ADD CONSTRAINT "fK_raw_motion_value_device_id" FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 3343 (class 2606 OID 17848)
-- Name: fk_device_user_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fk_device_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- TOC entry 3346 (class 2606 OID 17899)
-- Name: fk_geofence_poi_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY geofence
    ADD CONSTRAINT fk_geofence_poi_id FOREIGN KEY (poi_id) REFERENCES poi(id);


--
-- TOC entry 3361 (class 2606 OID 18075)
-- Name: fk_log_device_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY log
    ADD CONSTRAINT fk_log_device_id FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 3362 (class 2606 OID 18093)
-- Name: fk_motion_value_device_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY motion_values
    ADD CONSTRAINT fk_motion_value_device_id FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 3345 (class 2606 OID 17888)
-- Name: fk_poi_poi_type_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY poi
    ADD CONSTRAINT fk_poi_poi_type_id FOREIGN KEY (poitype_id) REFERENCES poi_type(id);


--
-- TOC entry 3364 (class 2606 OID 18121)
-- Name: fk_raw_waypoint_track_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY raw_waypoint
    ADD CONSTRAINT fk_raw_waypoint_track_id FOREIGN KEY (track_id) REFERENCES track(id);


--
-- TOC entry 3365 (class 2606 OID 18126)
-- Name: fk_raw_waypoint_transportation_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY raw_waypoint
    ADD CONSTRAINT fk_raw_waypoint_transportation_id FOREIGN KEY (transportation_id) REFERENCES transportation(id);


--
-- TOC entry 3352 (class 2606 OID 17968)
-- Name: fk_segment_poi_from_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_poi_from_id FOREIGN KEY (poi_from_id) REFERENCES poi(id);


--
-- TOC entry 3353 (class 2606 OID 17973)
-- Name: fk_segment_poi_to_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_poi_to_id FOREIGN KEY (poi_to_id) REFERENCES poi(id);


--
-- TOC entry 3354 (class 2606 OID 17978)
-- Name: fk_segment_transportation_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_transportation_id FOREIGN KEY (transportation_analyzed_id) REFERENCES transportation(id);


--
-- TOC entry 3355 (class 2606 OID 17983)
-- Name: fk_segment_transportation_route_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_transportation_route_id FOREIGN KEY (transportation_route_analyzed_id) REFERENCES transportation_route(id);


--
-- TOC entry 3356 (class 2606 OID 17988)
-- Name: fk_segment_waypoint_from_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_waypoint_from_id FOREIGN KEY (waypoint_from_id) REFERENCES waypoint(id);


--
-- TOC entry 3357 (class 2606 OID 17993)
-- Name: fk_segment_waypoint_to_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT fk_segment_waypoint_to_id FOREIGN KEY (waypoint_to_id) REFERENCES waypoint(id);


--
-- TOC entry 3360 (class 2606 OID 18080)
-- Name: fk_shedule_poi_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT fk_shedule_poi_id FOREIGN KEY (poi_id) REFERENCES poi(id);


--
-- TOC entry 3358 (class 2606 OID 18009)
-- Name: fk_shedule_shedule_day_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT fk_shedule_shedule_day_id FOREIGN KEY (shedule_day_id) REFERENCES schedule_day(id);


--
-- TOC entry 3359 (class 2606 OID 18014)
-- Name: fk_shedule_transportation_route_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT fk_shedule_transportation_route_id FOREIGN KEY (transportation_route_id) REFERENCES transportation_route(id);


--
-- TOC entry 3344 (class 2606 OID 17858)
-- Name: fk_track_device_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY track
    ADD CONSTRAINT fk_track_device_id FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 3347 (class 2606 OID 17931)
-- Name: fk_transportation_route_transportation_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY transportation_route
    ADD CONSTRAINT fk_transportation_route_transportation_id FOREIGN KEY (transportation_id) REFERENCES transportation(id);


--
-- TOC entry 3348 (class 2606 OID 17948)
-- Name: fk_waypoint_poi_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT fk_waypoint_poi_id FOREIGN KEY (poi_id) REFERENCES poi(id);


--
-- TOC entry 3351 (class 2606 OID 18098)
-- Name: fk_waypoint_track_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT fk_waypoint_track_id FOREIGN KEY (track_id) REFERENCES track(id);


--
-- TOC entry 3349 (class 2606 OID 17953)
-- Name: fk_waypoint_transportation_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT fk_waypoint_transportation_id FOREIGN KEY (transportation_superv_id) REFERENCES transportation(id);


--
-- TOC entry 3350 (class 2606 OID 17958)
-- Name: fk_waypoint_transportation_route_id; Type: FK CONSTRAINT; Schema: public; Owner: gisuser
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT fk_waypoint_transportation_route_id FOREIGN KEY (transportation_route_superv_id) REFERENCES transportation_route(id);


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-12-17 10:20:58 CET

--
-- PostgreSQL database dump complete
--

