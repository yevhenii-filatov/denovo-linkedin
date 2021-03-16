create table if not exists search_metadata
(
    id                 bigserial not null
        constraint search_metadata_pk
            primary key,
    query_url          varchar,
    search_page_source text
);

create table if not exists initial_data
(
    id                 bigserial    not null
        constraint initial_data_pk
            primary key,
    denovo_id          bigint       not null,
    updated_at         timestamp    null,
    first_name         varchar(255) not null,
    last_name          varchar(255) not null,
    firm_name          varchar(255) not null,
    searched           boolean      not null default false,
    no_results         boolean,
    search_metadata_id bigint
        constraint initial_data_search_metadata_fk
            references search_metadata (id)
            on delete cascade on update cascade
);
create index initial_data_search_metadata_idx on initial_data (search_metadata_id);

create table if not exists search_result
(
    id                     bigserial not null
        constraint search_result_pk
            primary key,
    collected_at           timestamp null,
    url                    varchar,
    title                  varchar(255),
    description            varchar(255),
    initial_data_record_id bigint    not null
        constraint search_result_initial_data_fk
            references initial_data (id)
            on delete cascade on update cascade
);
create index search_result_initial_data_fk on search_result (initial_data_record_id);

create table if not exists linkedin_profile
(
    id               bigserial not null
        constraint linkedin_profile_pk primary key,
    search_result_id bigint    not null,
    created_at       timestamp not null,
    updated_at       timestamp not null,
    profile_url      varchar   not null,
    foreign key (search_result_id)
        references search_result (id)
        on delete cascade on update cascade
);


create table if not exists linkedin_basic_profile_info
(
    id                    bigserial    not null
        constraint basic_profile_info_pk primary key,
    updated_at            timestamp    not null,
    header_section_source text         not null,
    about_section_source  text         null default null,
    full_name             varchar(255) not null,
    number_of_connections varchar(10)  not null,
    location              varchar(100) not null,
    cached_image_url      varchar      null default null,
    about                 text         null default null,
    linkedin_profile_id   bigint       not null
        constraint linkedin_basic_profile_info_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_basic_profile_info_linkedin_profile_idx on linkedin_basic_profile_info (linkedin_profile_id);

create type linkedin_job_type as enum (
    'FULL-TIME',
    'PART-TIME');

create table if not exists linkedin_experience
(
    id                  bigserial          not null
        constraint experience_pk primary key,
    updated_at          timestamp          not null,
    item_source         text               not null,
    job_type            linkedin_job_type  null default null,
    company_name        varchar(100)       not null,
    company_profile_url varchar            null default null,
    position            varchar(100)       not null,
    date_started        varchar(20)        null default null,
    date_finished       varchar(20)        null default null,
    total_duration      varchar(20)        null default null,
    location            varchar(100)       null default null,
    description         text               null default null,
    linkedin_profile_id bigint             not null
        constraint linkedin_experience_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_experience_linkedin_profile_idx on linkedin_experience (linkedin_profile_id);

create table if not exists linkedin_education
(
    id                       bigserial   not null
        constraint education_pk primary key,
    updated_at               timestamp   not null,
    item_source              text        not null,
    institution_name         varchar     not null,
    institution_profile_url  varchar     null default null,
    degree                   varchar     null default null,
    field_of_study           varchar     null default null,
    grade                    varchar     null default null,
    started_year             varchar(20) null default null,
    finished_year            varchar(20) null default null,
    activities_and_societies text        null default null,
    description              text        null default null,
    linkedin_profile_id      bigint      not null
        constraint linkedin_education_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_education_linkedin_profile_idx on linkedin_education (linkedin_profile_id);

create type linkedin_recommendation_type as enum ('RECEIVED', 'GIVEN');

create table if not exists linkedin_recommendation
(
    id                  bigserial                    not null
        constraint recommendation_pk primary key,
    updated_at          timestamp                    not null,
    item_source         text                         not null,
    type                linkedin_recommendation_type not null,
    person_full_name    varchar                      not null,
    person_profile_url  varchar                      not null,
    person_headline     varchar                      not null,
    person_extra_info   varchar                      not null,
    description         text                         not null,
    linkedin_profile_id bigint                       not null
        constraint linkedin_recommendation_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_recommendation_linkedin_profile_idx on linkedin_recommendation (linkedin_profile_id);

create table if not exists linkedin_license_certification
(
    id                  bigserial   not null
        constraint license_certification_pk primary key,
    updated_at          timestamp   not null,
    item_source         text        not null,
    name                varchar     not null,
    issuer              varchar     not null,
    issuer_profile_url  varchar     null default null,
    issued_date         varchar(20) null default null,
    expiration_date     varchar(30) null default null,
    credential_id       varchar(50) null default null,
    linkedin_profile_id bigint      not null
        constraint linkedin_license_certification_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_license_certification_linkedin_profile_idx on linkedin_license_certification (linkedin_profile_id);

create table if not exists linkedin_volunteer_experience
(
    id                  bigserial    not null
        constraint volunteer_experience_pk primary key,
    updated_at          timestamp    not null,
    item_source         text         not null,
    company_name        varchar(100) not null,
    company_profile_url varchar      null default null,
    position            varchar(100) not null,
    date_started        varchar(20)  not null,
    date_finished       varchar(20)  not null,
    total_duration      varchar(20)  not null,
    field_of_activity   varchar(100) null default null,
    description         text         null default null,
    linkedin_profile_id bigint       not null
        constraint linkedin_volunteer_experience_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_volunteer_experience_linkedin_profile_idx on linkedin_volunteer_experience (linkedin_profile_id);

create type linkedin_interest_type as enum ('INFLUENCER', 'COMPANY', 'GROUP', 'SCHOOL');

create table if not exists linkedin_interest
(
    id                  bigserial              not null
        constraint interest_pk primary key,
    updated_at          timestamp              not null,
    item_source         text                   not null,
    name                varchar                not null,
    type                linkedin_interest_type not null,
    profile_url         varchar                not null,
    headline            varchar                null default null,
    number_of_followers varchar(50)            not null,
    linkedin_profile_id bigint                 not null
        constraint linkedin_interest_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_interest_linkedin_profile_idx on linkedin_interest (linkedin_profile_id);

create table if not exists linkedin_skill
(
    id                     bigserial    not null
        constraint skill_pk primary key,
    updated_at             timestamp    not null,
    item_source            text         not null,
    name                   varchar(100) not null,
    category               varchar(100) not null,
    url                    varchar      null     default null,
    number_of_endorsements int          not null default 0,
    linkedin_profile_id    bigint       not null
        constraint linkedin_skill_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_skill_linkedin_profile_idx on linkedin_skill (linkedin_profile_id);

create table if not exists linkedin_endorsement
(
    id                         bigserial    not null
        constraint endorsement_pk primary key,
    updated_at                 timestamp    not null,
    item_source                text         not null,
    endorser_full_name         varchar(100) not null,
    endorser_headline          varchar      null default null,
    endorser_connection_degree varchar(10)  not null,
    endorser_profile_url       varchar      not null,
    skill_id                   bigint       not null
        constraint linkedin_endorsement_linkedin_skill_fk
            references linkedin_skill (id)
            on delete cascade on update cascade
);
create index linkedin_endorsement_linkedin_skill_idx on linkedin_endorsement (skill_id);

create type linkedin_activity_type as enum (
    'LIKED_THIS',
    'COMMENTED_ON',
    'SHARED_THIS',
    'REPLIED_TO_COMMENT',
    'JOB_UPDATE',
    'CUSTOM_POST',
    'LIKED_COMMENT');

create table if not exists linkedin_activity
(
    id                  bigserial              not null
        constraint activity_pk primary key,
    updated_at          timestamp              not null,
    type                linkedin_activity_type not null,
    linkedin_profile_id bigint                 not null
        constraint linkedin_activity_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_activity_linkedin_profile_idx on linkedin_activity (linkedin_profile_id);

create table if not exists linkedin_post
(
    id                        bigserial   not null
        constraint linkedin_post_pk primary key,
    url                       varchar     null     default null,
    item_source               text        not null,
    relative_publication_date varchar(20),
    collected_date            timestamp   not null,
    absolute_publication_date timestamp,
    author_name               varchar     not null,
    author_profile_url        varchar     not null,
    author_connection_degree  varchar     not null,
    author_headline           varchar     not null,
    content                   text        null     default null,
    number_of_comments        int         not null default 0,
    number_of_reactions       int         not null default 0,
    linkedin_activity_id      bigint      not null
        constraint linkedin_post_linkedin_activity_fk
            references linkedin_activity (id)
            on delete cascade on update cascade
);
create index linkedin_post_linkedin_activity_idx on linkedin_post (linkedin_activity_id);

create table if not exists linkedin_comment
(
    id                        bigserial   not null
        constraint linkedin_comment_pk primary key,
    url                       varchar     not null,
    item_source               text        not null,
    content                   varchar     not null,
    relative_publication_date varchar(20) not null,
    collected_date            timestamp   not null,
    absolute_publication_date timestamp   not null,
    number_of_reactions       int         not null default 0,
    number_of_replies         int         not null default 0,
    linkedin_post_id          bigint      not null
        constraint linkedin_comment_linkedin_post_fk
            references linkedin_post (id)
            on delete cascade on update cascade
);
create index linkedin_comment_linkedin_post_idx on linkedin_comment (linkedin_post_id);

create type linkedin_accomplishment_type as enum (
    'PUBLICATIONS',
    'LANGUAGES',
    'PATENTS',
    'PROJECTS',
    'HONORS_AND_AWARDS',
    'TEST_SCORES',
    'ORGANIZATIONS');

create table if not exists linkedin_accomplishment
(
    id                  bigserial                    not null
            constraint linkedin_accomplishment_pk primary key,
    updated_at          timestamp                    not null,
    item_source         text                         not null,
    title               varchar                      not null,
    description         varchar,
    type                linkedin_accomplishment_type not null,
    linkedin_profile_id bigint                       not null
        constraint linkedin_activity_linkedin_profile_fk
            references linkedin_profile (id)
            on delete cascade on update cascade
);
create index linkedin_accomplishment_linkedin_profile_idx on linkedin_accomplishment (linkedin_profile_id);
