create table public."Category"
(
    id   serial       not null
        constraint "Category_pkey"
            primary key,
    code varchar(255) not null
        constraint uk_8o4uo9exy72wl1c6jrn426b0l
            unique,
    name varchar(255) not null
);

alter table "Category"
    owner to galaksiya;

INSERT INTO public."Category" (code, name) VALUES ('hard_fact', 'Hard Fact');
INSERT INTO public."Category" (code, name) VALUES ('lifestyle', 'Lifestyle');
INSERT INTO public."Category" (code, name) VALUES ('introversion', 'Introversion');
INSERT INTO public."Category" (code, name) VALUES ('passion', 'Passion');

create table "Question"
(
    id             serial       not null
        constraint "Question_pkey"
            primary key,
    details        varchar(5000),
    "questionType" integer,
    value          varchar(255) not null,
    category       integer
        constraint fkf0jn81elfu0m0pct4cw5vcdms
            references "Category",
    child          integer
        constraint fk6v35n37cfbs5se5dofaplag3b
            references "Question"
);

alter table "Question"
    owner to galaksiya;

INSERT INTO public."Question"("value", category, child, "questionType", details) (SELECT 'What is your gender?', id,null,0,
	'{"options":["male","female","other"]}' from public."Category" WHERE code = 'hard_fact');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'How important is the gender of your partner?', id, null,0,
	'{"options":["not important","important","very important"]}' from public."Category" WHERE code = 'hard_fact');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'What age should your potential partner be?',id,null,2,
	'{"range": {"from":18,"to":140}}' from public."Category" WHERE code = 'hard_fact');
INSERT INTO public."Question"(value, category, child, "questionType", details) WITH cat as (SELECT id from public."Category" WHERE code = 'hard_fact'),
child as (SELECT id from public."Question" where value = 'What age should your potential partner be?') (select 'How important is the age of your partner to you?', cat.id, child.id,1,
'{"options":["not important","important","very important"],"condition":{"operator":"equals", "value":"very important"}}' from cat,child);
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'Do any children under the age of 18 live with you?', id,
 null,0, '{"options":["yes","sometimes","no"]}' from public."Category" WHERE code = 'hard_fact');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'How should your potential partner respond to this
question?', id,null,0, '{"options":["yes","sometimes","no"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'Could you imagine having children with your potential partner?',
	id,null,0, '{"options":["yes","maybe","no"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'How should your potential partner respond to this
question?', id,null,0, '{"options":["yes","maybe","no"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'What is your marital status?', id,
	null,0, '{"options":["never married","separated","divorced","widowed"]}' from public."Category" WHERE code = 'hard_fact');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'How often do your drink alcohol?', id,
	null,0, '{"options":["never","once or twice a year","once or twice a month","once or twice a week","I''m drinking my 3rd mojito now, and it''s only 11am"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'How often do you smoke?', id,
	null,0, '{"options":["never","once or twice a year","socially","frequently"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'What is your attitude towards drugs?', id,
	null,0, '{"options":["I''m completely opposed","I''ve been know to dabble","drugs enrich my life"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'You are looking for...', id,
	null,0, '{"options":["friendship","a hot date","an affair","a short-term relationship","a long-term relationship"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'Would you like to get married?', id,
	null,0, '{"options":["yes","probably","eventually","no"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'What is your ideal living arrangement?', id,
	null,0, '{"options":["cohabitation","separate flat / room in the same building","separate flats in the same area","weekend-relationship","long distance relationship"]}' from public."Category" WHERE code = 'lifestyle');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'Do you enjoy spending time alone?', id,
	null,0, '{"options":["most of the time","often","sometimes","rarely","not at all"]}' from public."Category" WHERE code = 'introversion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'When you''re alone, do you get lonely quickly?', id,
	null,0, '{"options":["most of the time","often","sometimes","rarely","not at all"]}' from public."Category" WHERE code = 'introversion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'Do you enjoy going on holiday by yourself?', id,
	null,0, '{"options":["most of the time","often","sometimes","rarely","not at all"]}' from public."Category" WHERE code = 'introversion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'I consciously take "me time"', id,
	null,0, '{"options":["most of the time","often","sometimes","rarely","not at all"]}' from public."Category" WHERE code = 'introversion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'Should one keep little secrets from one''s partner?', id,
	null,0, '{"options":["most of the time","often","sometimes","rarely","not at all"]}' from public."Category" WHERE code = 'introversion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'How often do you think about sex?', id,
	null,0, '{"options":["a few times a day","daily","a few times a week","a few times a month","rarely"]}' from public."Category" WHERE code = 'passion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'If you were alone on a desert island, how long would you
last before pleasuring yourself?', id,null,0, '{"options":["less than a day","one day","one week","one month","I''d never do something like that"]}' from public."Category" WHERE code = 'passion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'How often would you like to have sex with your
prospective partner?', id,null,0, '{"options":["every day","a few times a week","once a week","every two weeks","infrequently","rarely"]}' from public."Category" WHERE code = 'passion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'Do you like trying out new things in bed and experimenting?', id,
	null,0, '{"options":["Yes, definitely!","Now and then - why not?","I''d give it a try","I don''t know","Absolutely not"]}' from public."Category" WHERE code = 'passion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'I can enjoy sex without love', id,
	null,0, '{"options":["always","often","sometimes","rarely","never"]}' from public."Category" WHERE code = 'passion');
INSERT INTO public."Question"(value, category, child, "questionType", details) (SELECT 'For me, a stable relationship is a prerequisite for really good sex',
	id,null,0, '{"options":["always","often","sometimes","rarely","never"]}' from public."Category" WHERE code = 'passion');

create table "AnswerGroup"
(
    id        serial       not null
        constraint "AnswerGroup_pkey"
            primary key,
    date      bigint       not null,
    "userKey" varchar(255) not null
);

alter table "AnswerGroup"
    owner to galaksiya;

create table "Answer"
(
    id            serial       not null
        constraint "Answer_pkey"
            primary key,
    value         varchar(255) not null,
    "answerGroup" integer      not null
        constraint fkq1fn6fbi3vxnswr95c62hue2l
            references "AnswerGroup",
    question      integer      not null
        constraint fkd75tm124g7r00j4xe09ru4s3a
            references "Question"
);

alter table "Answer"
    owner to galaksiya;


