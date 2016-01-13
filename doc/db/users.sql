INSERT INTO public."user" ( id , username , password ) VALUES ( '96778420-fdb9-41e1-b734-1e45f4c054cf' , 'stefan' , 'stefan' );
INSERT INTO public."user" ( id , username , password ) VALUES ( '4afb127c-9d65-4caf-9d86-4dfa45ed6d78' , 'philip' , 'philip' );
INSERT INTO public."user" ( id , username , password ) VALUES ( '257c5eba-2df0-4982-9fbb-47866bed9486' , 'florian' , 'florian' );
INSERT INTO public."user" ( id , username , password ) VALUES ( 'a44bd99a-78e9-473c-a344-8f77dd30514e' , 'tobias' , 'tobias' );
INSERT INTO public."user" ( id , username , password ) VALUES ( '6bf3daba-063c-4152-81f3-709028d41fb7' , 'nicolaj' , 'nicolaj' );
INSERT INTO public."user" ( id , username , password ) VALUES ( '907fa26f-f10d-4c4d-9c84-8d8d60758096' , 'nino' , 'nino' );
INSERT INTO public."user" ( id , username , password ) VALUES ( 'f1be54e7-b83b-4389-8e97-a5ca1fa7008f' , 'anonym' , 'anonym' );
INSERT INTO public."user" ( id , username , password ) VALUES ( 'd40fea61-da33-4db2-a0d1-72c7bee062d3' , 'lucas' , 'lucas' );
INSERT INTO public."user" ( id , username , password ) VALUES ( '6a6d5d1d-1c44-480b-acd1-36bcf4d58709' , 'flo' , 'flo' );
INSERT INTO public."user" ( id , username , password ) VALUES ( 'f06743f9-66e9-4f31-bd5a-14f0496ddd1f' , 'marco' , 'marco' );

INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( '5108d8a1-52dc-4219-b407-0e0611cc73db' , '' , '96778420-fdb9-41e1-b734-1e45f4c054cf' , '694e1b5ad348a806' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( '59d562ad-f3f8-487e-bd8c-53b79e1d90e2' , '' , '4afb127c-9d65-4caf-9d86-4dfa45ed6d78' , '7476e925da116582' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( 'f90a4ab1-3d88-4e1f-958c-f9c8eb0c0913' , '' , '257c5eba-2df0-4982-9fbb-47866bed9486' , '8054f56f74c8c97a' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( 'aea88415-fc08-4e1c-be1d-65ae2b4f7fe4' , '' , 'a44bd99a-78e9-473c-a344-8f77dd30514e' , '886d9fedcf62b929' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( '938976b2-8d05-483d-a6cf-aa36fe78b258' , '' , '6bf3daba-063c-4152-81f3-709028d41fb7' , 'ad3bb99ed4169c0f' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( '85d08858-656a-4795-a175-0a125ac93e5f' , '' , '907fa26f-f10d-4c4d-9c84-8d8d60758096' , 'b20e562a611bba1e' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( '991f2b1b-595c-4312-8c72-730bf3d6701d' , '' , 'f1be54e7-b83b-4389-8e97-a5ca1fa7008f' , 'cd5725e6f6de79a9' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( '89a9f6a6-5e20-4d49-bc50-404c535430af' , '' , 'd40fea61-da33-4db2-a0d1-72c7bee062d3' , 'ea0515981c1d2fd1' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( '458f2def-4cf7-46f4-93de-e914ebe2e895' , '' , '6a6d5d1d-1c44-480b-acd1-36bcf4d58709' , 'eb84120e105f0b0f' );
INSERT INTO public."device" ( id , token , user_id , device_id ) VALUES ( 'a2a8a7be-40ac-4457-b68c-bcd94835fb74' , '' , 'f06743f9-66e9-4f31-bd5a-14f0496ddd1f' , 'f402f875a975b371' );

INSERT INTO public.transportation(id, name, max_speed, avarage_speed, color) VALUES ('fc155abd-c847-43e6-947f-ce48d69ddec8', 'Walking', NULL, NULL, NULL);
INSERT INTO public.transportation(id, name, max_speed, avarage_speed, color) VALUES ('a809634a-b78e-40fc-8efe-0217c5856695', 'Bicycle', NULL, NULL, NULL);
INSERT INTO public.transportation(id, name, max_speed, avarage_speed, color) VALUES ('a46dfb1c-b987-44b6-b4ad-ae63a1c6d8ae', 'Bus', NULL, NULL, NULL);
INSERT INTO public.transportation(id, name, max_speed, avarage_speed, color) VALUES ('02d420e8-7e0a-4b9b-b28c-8587c105c9a8', 'Train', NULL, NULL, NULL);
INSERT INTO public.transportation(id, name, max_speed, avarage_speed, color) VALUES ('ee36de6d-68a7-4a3c-ab1d-9d2889539e1b', 'Car', NULL, NULL, NULL);

