create table if not exists search_metadata
(
    id                 bigserial    not null
        constraint search_metadata_pkey
            primary key,
    query_url          varchar not null,
    search_page_source text not null
);

create table if not exists initial_data
(
    id                 bigserial    not null
        constraint initial_data_pkey
            primary key,
    updated_at         timestamp null,
    first_name         varchar(255) not null,
    last_name          varchar(255) not null,
    firm_name          varchar(255) not null,
    searched           boolean      not null default false,
    no_results         boolean,
    search_metadata_id bigint
        constraint fkejuoknqmq8r7s218mhaw2yt1g
            references search_metadata
);

create table if not exists search_result
(
    id                     bigserial not null
        constraint search_result_pkey
            primary key,
    collected_at         timestamp null,
    url                    varchar,
    title                  varchar(255),
    description            varchar(255),
    initial_data_record_id bigint    not null
        constraint fk5ym5kgbqfm4m40qmeglwub2o
            references initial_data
);

