--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2 (Debian 16.2-1.pgdg120+2)
-- Dumped by pg_dump version 16.4

-- Started on 2025-10-31 12:53:52 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3432 (class 1262 OID 24611)
-- Name: icy_cupcakes_database; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE icy_cupcakes_database WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE icy_cupcakes_database OWNER TO postgres;

\connect icy_cupcakes_database

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 226 (class 1259 OID 24694)
-- Name: cupcakes_in_a_order; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cupcakes_in_a_order (
                                            order_id integer NOT NULL,
                                            udc_id integer NOT NULL,
                                            amount integer DEFAULT 1
);


ALTER TABLE public.cupcakes_in_a_order OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 24703)
-- Name: discount_code; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discount_code (
                                      discount_id integer NOT NULL,
                                      discount_code character varying NOT NULL,
                                      discount real NOT NULL
);


ALTER TABLE public.discount_code OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 24702)
-- Name: discount_code_discount_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discount_code_discount_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.discount_code_discount_id_seq OWNER TO postgres;

--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 228
-- Name: discount_code_discount_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.discount_code_discount_id_seq OWNED BY public.discount_code.discount_id;


--
-- TOC entry 223 (class 1259 OID 24679)
-- Name: icing; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.icing (
                              icing_id integer NOT NULL,
                              icing_name character varying NOT NULL,
                              icing_price real NOT NULL
);


ALTER TABLE public.icing OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24678)
-- Name: icing_icing_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.icing_icing_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.icing_icing_id_seq OWNER TO postgres;

--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 222
-- Name: icing_icing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.icing_icing_id_seq OWNED BY public.icing.icing_id;


--
-- TOC entry 219 (class 1259 OID 24663)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
                               id integer NOT NULL,
                               user_id integer NOT NULL,
                               date date NOT NULL,
                               applied_discount integer
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 24662)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_id_seq OWNER TO postgres;

--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 218
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- TOC entry 227 (class 1259 OID 24698)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
                              user_id integer NOT NULL,
                              roles integer NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 24670)
-- Name: the_bottoms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.the_bottoms (
                                    bottom_id integer NOT NULL,
                                    bottom_name character varying NOT NULL,
                                    bottom_price real NOT NULL
);


ALTER TABLE public.the_bottoms OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 24669)
-- Name: the_bottoms_bottom_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.the_bottoms_bottom_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.the_bottoms_bottom_id_seq OWNER TO postgres;

--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 220
-- Name: the_bottoms_bottom_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.the_bottoms_bottom_id_seq OWNED BY public.the_bottoms.bottom_id;


--
-- TOC entry 225 (class 1259 OID 24688)
-- Name: user_defined_cupcakes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_defined_cupcakes (
                                              udc_id integer NOT NULL,
                                              udc_bottom integer NOT NULL,
                                              udc_icing integer NOT NULL
);


ALTER TABLE public.user_defined_cupcakes OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 24687)
-- Name: user_defined_cupcakes_udc_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_defined_cupcakes_udc_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_defined_cupcakes_udc_id_seq OWNER TO postgres;

--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 224
-- Name: user_defined_cupcakes_udc_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_defined_cupcakes_udc_id_seq OWNED BY public.user_defined_cupcakes.udc_id;


--
-- TOC entry 216 (class 1259 OID 24646)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
                              id integer NOT NULL,
                              first_name character varying,
                              last_name character varying,
                              zip_code integer,
                              street_name character varying,
                              house_number integer,
                              floor character varying,
                              email character varying NOT NULL,
                              password character varying NOT NULL,
                              wallet real DEFAULT 0
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 24645)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 217 (class 1259 OID 24655)
-- Name: zip_code; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zip_code (
                                 zip_code integer NOT NULL,
                                 city character varying NOT NULL
);


ALTER TABLE public.zip_code OWNER TO postgres;

--
-- TOC entry 3247 (class 2604 OID 24706)
-- Name: discount_code discount_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_code ALTER COLUMN discount_id SET DEFAULT nextval('public.discount_code_discount_id_seq'::regclass);


--
-- TOC entry 3244 (class 2604 OID 24682)
-- Name: icing icing_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.icing ALTER COLUMN icing_id SET DEFAULT nextval('public.icing_icing_id_seq'::regclass);


--
-- TOC entry 3242 (class 2604 OID 24666)
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- TOC entry 3243 (class 2604 OID 24673)
-- Name: the_bottoms bottom_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.the_bottoms ALTER COLUMN bottom_id SET DEFAULT nextval('public.the_bottoms_bottom_id_seq'::regclass);


--
-- TOC entry 3245 (class 2604 OID 24691)
-- Name: user_defined_cupcakes udc_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_defined_cupcakes ALTER COLUMN udc_id SET DEFAULT nextval('public.user_defined_cupcakes_udc_id_seq'::regclass);


--
-- TOC entry 3240 (class 2604 OID 24649)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3423 (class 0 OID 24694)
-- Dependencies: 226
-- Data for Name: cupcakes_in_a_order; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (63, 48, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (63, 49, 69);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (65, 50, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (67, 52, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (68, 53, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (69, 54, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (70, 55, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (78, 56, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (105, 57, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (109, 58, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (110, 59, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (116, 61, 11);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (116, 62, 13);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (117, 63, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (130, 64, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (141, 65, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (163, 66, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (174, 67, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (174, 68, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (176, 69, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (186, 70, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (187, 71, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (188, 72, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (189, 73, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (206, 76, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (207, 77, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (209, 78, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (210, 79, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (211, 80, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (225, 81, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (263, 82, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (281, 83, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (290, 84, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (290, 85, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (291, 86, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (292, 87, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (293, 88, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (295, 89, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (296, 90, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (298, 91, 1);
INSERT INTO public.cupcakes_in_a_order (order_id, udc_id, amount) VALUES (302, 92, 1);


--
-- TOC entry 3426 (class 0 OID 24703)
-- Dependencies: 229
-- Data for Name: discount_code; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.discount_code (discount_id, discount_code, discount) VALUES (0, 'itsNullTimeBaby!', 0);
INSERT INTO public.discount_code (discount_id, discount_code, discount) VALUES (2, '25FALL', 25);
INSERT INTO public.discount_code (discount_id, discount_code, discount) VALUES (3, 'Jesper', 50);
INSERT INTO public.discount_code (discount_id, discount_code, discount) VALUES (4, 'TeamICE', 100);
INSERT INTO public.discount_code (discount_id, discount_code, discount) VALUES (1, '10Halloween', 10);
INSERT INTO public.discount_code (discount_id, discount_code, discount) VALUES (5, 'ServeAndProtect', -1);


--
-- TOC entry 3420 (class 0 OID 24679)
-- Dependencies: 223
-- Data for Name: icing; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (1, 'Chocolate', 5);
INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (2, 'Blueberry', 5);
INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (3, 'Raspberry', 5);
INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (4, 'Crispy', 6);
INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (5, 'Strawberry', 6);
INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (7, 'Orange', 8);
INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (8, 'Lemon', 8);
INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (6, 'Rum&Raisin', 7);
INSERT INTO public.icing (icing_id, icing_name, icing_price) VALUES (9, 'Blue Cheese', 9);


--
-- TOC entry 3416 (class 0 OID 24663)
-- Dependencies: 219
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (63, 4, '2025-10-23', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (65, 4, '2025-10-23', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (67, 4, '2025-10-23', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (68, 4, '2025-10-23', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (69, 4, '2025-10-23', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (70, 4, '2025-10-23', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (78, 4, '2025-10-23', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (105, 5, '2025-10-24', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (109, 4, '2025-10-24', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (110, 4, '2025-10-24', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (116, 4, '2025-10-26', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (117, 4, '2025-10-26', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (130, 4, '2025-10-27', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (141, 4, '2025-10-27', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (163, 4, '2025-10-28', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (174, 3, '2025-10-28', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (176, 4, '2025-10-28', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (186, 4, '2025-10-29', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (187, 4, '2025-10-29', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (188, 4, '2025-10-29', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (189, 4, '2025-10-29', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (206, 4, '2025-10-30', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (207, 4, '2025-10-30', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (208, 4, '2025-10-30', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (209, 4, '2025-10-30', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (210, 4, '2025-10-30', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (211, 4, '2025-10-30', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (225, 4, '2025-10-30', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (262, 4, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (263, 4, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (281, 29, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (290, 4, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (291, 4, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (292, 4, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (293, 4, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (295, 31, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (296, 32, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (298, 34, '2025-10-31', 0);
INSERT INTO public.orders (id, user_id, date, applied_discount) VALUES (302, 4, '2025-10-31', 0);


--
-- TOC entry 3424 (class 0 OID 24698)
-- Dependencies: 227
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.roles (user_id, roles) VALUES (4, 1);


--
-- TOC entry 3418 (class 0 OID 24670)
-- Dependencies: 221
-- Data for Name: the_bottoms; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.the_bottoms (bottom_id, bottom_name, bottom_price) VALUES (3, 'Nutmeg', 5);
INSERT INTO public.the_bottoms (bottom_id, bottom_name, bottom_price) VALUES (4, 'Pistacio', 6);
INSERT INTO public.the_bottoms (bottom_id, bottom_name, bottom_price) VALUES (5, 'Almond', 7);
INSERT INTO public.the_bottoms (bottom_id, bottom_name, bottom_price) VALUES (1, 'Chocolate', 5);
INSERT INTO public.the_bottoms (bottom_id, bottom_name, bottom_price) VALUES (2, 'Vanilla', 5);


--
-- TOC entry 3422 (class 0 OID 24688)
-- Dependencies: 225
-- Data for Name: user_defined_cupcakes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (1, 2, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (2, 4, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (3, 3, 6);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (4, 4, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (5, 2, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (6, 3, 7);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (7, 3, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (8, 3, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (9, 3, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (10, 3, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (11, 4, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (12, 4, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (13, 4, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (14, 2, 6);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (15, 3, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (16, 4, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (17, 2, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (18, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (19, 5, 7);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (20, 2, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (21, 2, 6);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (22, 2, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (23, 2, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (24, 2, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (25, 4, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (26, 1, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (27, 2, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (28, 2, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (29, 2, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (30, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (31, 2, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (32, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (33, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (34, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (35, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (36, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (37, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (38, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (39, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (41, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (46, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (48, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (49, 3, 6);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (50, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (52, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (53, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (54, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (55, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (56, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (57, 2, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (58, 2, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (59, 2, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (61, 2, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (62, 3, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (63, 2, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (64, 1, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (65, 3, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (66, 3, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (67, 3, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (68, 4, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (69, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (70, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (71, 1, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (72, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (73, 1, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (75, 5, 9);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (76, 4, 9);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (77, 2, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (78, 1, 5);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (79, 2, 1);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (80, 5, 7);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (81, 2, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (82, 2, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (83, 4, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (84, 4, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (85, 4, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (86, 3, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (87, 5, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (88, 4, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (89, 4, 4);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (90, 4, 2);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (91, 4, 3);
INSERT INTO public.user_defined_cupcakes (udc_id, udc_bottom, udc_icing) VALUES (92, 3, 3);


--
-- TOC entry 3413 (class 0 OID 24646)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (25, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '5@5.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (26, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '5@6.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (5, 'jose', 'Libseich', 1829, 'rolighedsvej', 1, '1', '1@1', '1', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (6, 'Albert', 'Johansen', 2100, 'allevej', 1, '1', 'x@t.dk', '1', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (7, 'Rune', 'Runesen', 1800, 'jens alle', 2, '2', 'Dex@ter.lab', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (8, 'Jens', 'Rygmark', 2459, 'Byvejen', 3, '5', 'buzz@lightyear.zap', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (9, 'Hans', 'Toft', 1253, 'droningensgade', 2, '2', 're@re.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (10, 'Gilbert', 'Bouju', 1243, 'Tværgade', 23, '2', 'r@d.fr', '1', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (11, 'Andreas', 'Andersen', 2200, 'hovedgaden', 123, '5', 'd@d.dk', '1', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (12, 'Tor', 'Jepsen', 1800, 'Kystvejen', 56, '5', 'ex@ht.dk', '1', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (13, 'Christian', 'Hoffman', 1245, 'markvej', 312, '3', '54@54.dk', '12', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (14, 'Amalie', 'Jensen', 5421, 'høstgårdsvej', 34, '1', '1@1.dk', '1', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (27, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '6@6.dk', '123', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (28, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '45@45.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (29, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '46@46.dk', '1234', -48);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (30, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '9@9.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (31, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '1234@1234.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (32, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '42@42.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (33, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '34@34.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (34, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', 'le@bon.fr', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (35, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', 'jj@kk.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (36, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '99@cents.50', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (3, 'John', 'Doe', 2200, 'Amagertorpsvej', 12, '1', '1@ricegiven.dk', 'die', 420);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (15, 'Anders', 'Skibsberg', 2900, 'Hambro''s Allé', 1, '1', 'j@lnc.fr', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (16, 'Joakim', 'Johansen', 2900, 'Centergaden ', 3, '1', '123@123.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (17, 'Hans', 'Kendser', 3800, 'Huldgade', 5, '1', '12@12.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (18, 'Casper', 'Fridtjoffsen', 4538, 'Fredensvej', 34, '1', '43@43', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (19, 'Frederik', 'Almadôttir', 6589, 'Håbsvej', 27, '1', '23@23.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (20, 'Luke', 'Persson', 3700, 'Landevejen', 13, '1', '123@123.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (21, 'Hector', 'Filicius', 3740, 'Hogsmead Hovedgade', 12, '1', '89@89.ds', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (22, 'Randi', 'Thorsen', 3751, 'A.N. Hansens alle', 16, '12', 'popo@yoyo.soso', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (23, 'Oliver', 'Thorsen', 3760, 'A.N. Hansens alle', 16, '1', 'ebg@hgf.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (24, 'Dolph', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', 'dd@mm.or', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (37, 'Emil', 'Thorsen', 2900, 'A.N. Hansens alle', 16, '1', '50@cents.dk', '1234', 0);
INSERT INTO public.users (id, first_name, last_name, zip_code, street_name, house_number, floor, email, password, wallet) VALUES (4, 'emil', 'thor', 2200, 'lykkevej', 13, '1', 'ex@tv.dk', '1234', 3308);


--
-- TOC entry 3414 (class 0 OID 24655)
-- Dependencies: 217
-- Data for Name: zip_code; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.zip_code (zip_code, city) VALUES (2200, 'nørrebro');


--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 228
-- Name: discount_code_discount_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_code_discount_id_seq', 5, true);


--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 222
-- Name: icing_icing_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.icing_icing_id_seq', 9, true);


--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 218
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_id_seq', 303, true);


--
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 220
-- Name: the_bottoms_bottom_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.the_bottoms_bottom_id_seq', 5, true);


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 224
-- Name: user_defined_cupcakes_udc_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_defined_cupcakes_udc_id_seq', 92, true);


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 37, true);


--
-- TOC entry 3261 (class 2606 OID 24710)
-- Name: discount_code discount_code_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_code
    ADD CONSTRAINT discount_code_pkey PRIMARY KEY (discount_id);


--
-- TOC entry 3257 (class 2606 OID 24686)
-- Name: icing icing_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.icing
    ADD CONSTRAINT icing_pkey PRIMARY KEY (icing_id);


--
-- TOC entry 3253 (class 2606 OID 24668)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 3255 (class 2606 OID 24677)
-- Name: the_bottoms the_bottoms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.the_bottoms
    ADD CONSTRAINT the_bottoms_pkey PRIMARY KEY (bottom_id);


--
-- TOC entry 3259 (class 2606 OID 24693)
-- Name: user_defined_cupcakes user_defined_cupcakes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_defined_cupcakes
    ADD CONSTRAINT user_defined_cupcakes_pkey PRIMARY KEY (udc_id);


--
-- TOC entry 3249 (class 2606 OID 24654)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3251 (class 2606 OID 24661)
-- Name: zip_code zip_code_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zip_code
    ADD CONSTRAINT zip_code_pkey PRIMARY KEY (zip_code);


--
-- TOC entry 3268 (class 2606 OID 24746)
-- Name: roles admin_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT admin_user FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;


--
-- TOC entry 3264 (class 2606 OID 24726)
-- Name: user_defined_cupcakes bottom; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_defined_cupcakes
    ADD CONSTRAINT bottom FOREIGN KEY (udc_bottom) REFERENCES public.the_bottoms(bottom_id) NOT VALID;


--
-- TOC entry 3262 (class 2606 OID 24721)
-- Name: orders discount_applied; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT discount_applied FOREIGN KEY (applied_discount) REFERENCES public.discount_code(discount_id) NOT VALID;


--
-- TOC entry 3265 (class 2606 OID 24731)
-- Name: user_defined_cupcakes icing; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_defined_cupcakes
    ADD CONSTRAINT icing FOREIGN KEY (udc_icing) REFERENCES public.icing(icing_id) NOT VALID;


--
-- TOC entry 3266 (class 2606 OID 24736)
-- Name: cupcakes_in_a_order orderid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cupcakes_in_a_order
    ADD CONSTRAINT orderid FOREIGN KEY (order_id) REFERENCES public.orders(id) NOT VALID;


--
-- TOC entry 3267 (class 2606 OID 24741)
-- Name: cupcakes_in_a_order udc_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cupcakes_in_a_order
    ADD CONSTRAINT udc_id FOREIGN KEY (udc_id) REFERENCES public.user_defined_cupcakes(udc_id) NOT VALID;


--
-- TOC entry 3263 (class 2606 OID 24716)
-- Name: orders uid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT uid FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;


-- Completed on 2025-10-31 12:53:52 UTC

--
-- PostgreSQL database dump complete
--

