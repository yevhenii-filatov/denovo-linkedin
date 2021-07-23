create table linkedin.google_search_metadata
(
    id                 bigint auto_increment
        primary key,
    query_url          varchar(1000) not null,
    search_page_source mediumtext    not null
);

create table linkedin.google_initial_data
(
    id                 bigint auto_increment
        primary key,
    denovo_id          bigint        null,
    firm_name          varchar(255)  not null,
    first_name         varchar(255)  not null,
    last_name          varchar(255)  not null,
    linkedin_url       varchar(1000) null,
    middle_name        varchar(255)  not null,
    no_results         bit           null,
    searched           bit           not null,
    updated_at         datetime      null,
    search_metadata_id bigint        null,
    constraint FK4wsu2xlb3da6hinkke88qe13n
        foreign key (search_metadata_id) references linkedin.google_search_metadata (id)
            on update cascade on delete cascade
);

create table linkedin.google_search_result
(
    id                     bigint auto_increment
        primary key,
    collected_at           datetime     null,
    description            varchar(255) null,
    search_position        int          null,
    search_step            int          null,
    title                  varchar(255) null,
    url                    varchar(255) null,
    initial_data_record_id bigint       not null,
    constraint FKmcih26vedmeikk37bgy6u35lv
        foreign key (initial_data_record_id) references linkedin.google_initial_data (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_not_reusable_profile
(
    id                bigint auto_increment
        primary key,
    error_description text         null,
    optional_fields   varchar(255) null,
    search_result_id  bigint       null,
    constraint FKfl3q02wbhm09s97chvm1d91oi
        foreign key (search_result_id) references linkedin.google_search_result (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_profile
(
    id               bigint auto_increment
        primary key,
    created_at       datetime null,
    profile_url      text     null,
    updated_at       datetime null,
    search_result_id bigint   null,
    constraint FKn7toksq5skrihm7jial4chwb1
        foreign key (search_result_id) references linkedin.google_search_result (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_accomplishment
(
    id                  bigint auto_increment
        primary key,
    description         text         null,
    item_source         text         null,
    type                varchar(255) null,
    title               text         null,
    updated_at          datetime     null,
    linkedin_profile_id bigint       null,
    constraint FKqtodmhpuckwf0d8l5hj66n9pf
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_activity
(
    id                  bigint auto_increment
        primary key,
    type                varchar(255) null,
    updated_at          datetime     null,
    linkedin_profile_id bigint       null,
    constraint FKp2m0jfky7b3fnxwlpecwlh3r2
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_basic_profile_info
(
    id                    bigint auto_increment
        primary key,
    about                 text     null,
    about_section_source  text     null,
    cached_image_url      text     null,
    full_name             text     null,
    header_section_source text     null,
    location              text     null,
    number_of_connections text     null,
    updated_at            datetime null,
    linkedin_profile_id   bigint   null,
    constraint FKq9kvwan12srdruscc5fcnho3v
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_education
(
    id                       bigint auto_increment
        primary key,
    activities_and_societies text     null,
    degree                   text     null,
    description              text     null,
    field_of_study           text     null,
    finished_year            text     null,
    grade                    text     null,
    institution_name         text     null,
    institution_profile_url  text     null,
    item_source              text     null,
    started_year             text     null,
    updated_at               datetime null,
    linkedin_profile_id      bigint   null,
    constraint FKeyinry4tb7s9qfj60wfv2suqx
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_experience
(
    id                      bigint auto_increment
        primary key,
    company_name            text         null,
    company_profile_url     text         null,
    date_finished           text         null,
    date_finished_timestamp date         null,
    date_started            text         null,
    date_started_timestamp  date         null,
    description             text         null,
    item_source             text         null,
    job_type                varchar(255) null,
    location                text         null,
    position                text         null,
    total_duration          text         null,
    updated_at              datetime     null,
    linkedin_profile_id     bigint       null,
    constraint FKkdrhrj9qha0orveiw5ag17yoo
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_interest
(
    id                  bigint auto_increment
        primary key,
    headline            text         null,
    item_source         text         null,
    type                varchar(255) null,
    name                text         null,
    number_of_followers text         null,
    profile_url         text         null,
    updated_at          datetime     null,
    linkedin_profile_id bigint       null,
    constraint FK6tev6q9lsp9en80o2uk22si1g
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_license_certification
(
    id                  bigint auto_increment
        primary key,
    credential_id       text     null,
    expiration_date     text     null,
    issued_date         text     null,
    issuer              text     null,
    issuer_profile_url  text     null,
    item_source         text     null,
    name                text     null,
    updated_at          datetime null,
    linkedin_profile_id bigint   null,
    constraint FKg291t7ci9vtd9a2sanlpkuyee
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_post
(
    id                        bigint auto_increment
        primary key,
    absolute_publication_date datetime null,
    author_connection_degree  text     null,
    author_headline           text     null,
    author_name               text     null,
    author_profile_url        text     null,
    collected_date            datetime null,
    content                   text     null,
    item_source               text     null,
    number_of_comments        int      null,
    number_of_reactions       int      null,
    relative_publication_date text     null,
    url                       text     null,
    linkedin_activity_id      bigint   null,
    constraint FKsirqv45600eovwpfha5quyttw
        foreign key (linkedin_activity_id) references linkedin.linkedin_activity (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_comment
(
    id                        bigint auto_increment
        primary key,
    absolute_publication_date datetime null,
    collected_date            datetime null,
    content                   text     null,
    item_source               text     null,
    number_of_reactions       int      null,
    number_of_replies         int      null,
    relative_publication_date text     null,
    url                       text     null,
    linkedin_post_id          bigint   null,
    constraint FK8yk7l4pjx1ak5vdph810vw3un
        foreign key (linkedin_post_id) references linkedin.linkedin_post (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_recommendation
(
    id                  bigint auto_increment
        primary key,
    description         text         null,
    item_source         text         null,
    type                varchar(255) null,
    person_extra_info   text         null,
    person_full_name    text         null,
    person_headline     text         null,
    person_profile_url  text         null,
    updated_at          datetime     null,
    linkedin_profile_id bigint       null,
    constraint FK8aanwcq06tggnmb7l0w4j6i4m
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_skill
(
    id                     bigint auto_increment
        primary key,
    category               text     null,
    item_source            text     null,
    name                   text     null,
    number_of_endorsements int      null,
    updated_at             datetime null,
    url                    text     null,
    linkedin_profile_id    bigint   null,
    constraint FKr62ix4pi1soh3767da8doiwfc
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_endorsement
(
    id                         bigint auto_increment
        primary key,
    endorser_connection_degree text     null,
    endorser_full_name         text     null,
    endorser_headline          text     null,
    endorser_profile_url       text     null,
    item_source                text     null,
    updated_at                 datetime null,
    skill_id                   bigint   null,
    constraint FKojt5gse81i69ea7av20ikk031
        foreign key (skill_id) references linkedin.linkedin_skill (id)
            on update cascade on delete cascade
);

create table linkedin.linkedin_volunteer_experience
(
    id                  bigint auto_increment
        primary key,
    company_name        text     null,
    company_profile_url text     null,
    date_finished       text     null,
    date_started        text     null,
    description         text     null,
    field_of_activity   text     null,
    item_source         text     null,
    position            text     null,
    total_duration      text     null,
    updated_at          datetime null,
    linkedin_profile_id bigint   null,
    constraint FKg96fuxweae5evr1nuq7g6nuaq
        foreign key (linkedin_profile_id) references linkedin.linkedin_profile (id)
            on update cascade on delete cascade
);
