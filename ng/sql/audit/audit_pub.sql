-- Autogenerated on Fri May  8 09:42:01 2009 by mkaudit.pl

create table audit.pub (
    uniquename text not null
  , volume character varying(255)
  , pyear character varying(255)
  , issue character varying(255)
  , series_name character varying(255)
  , volumetitle text
  , miniref character varying(255)
  , pub_id integer not null
  , type_id integer not null
  , title text
  , publisher character varying(255)
  , is_obsolete boolean
  , pubplace character varying(255)
  , pages character varying(255)
) inherits (audit.audit);

create or replace function audit.audit_pub_insert_proc()
returns trigger
as $$
BEGIN
  raise exception 'Cannot insert directly into audit.pub. Use one of the child tables.';
END;
$$ language plpgsql;
create trigger pub_insert_tr before insert on audit.pub
    for each statement execute procedure audit.audit_pub_insert_proc();
grant select on audit.pub to chado_ro_role;
grant select, insert on audit.pub to chado_rw_role;
grant execute on function audit.audit_pub_insert_proc() to chado_rw_role;


create table audit.pub_insert (
    constraint pub_insert_ck check (type = 'INSERT')
) inherits (audit.pub);
alter table audit.pub_insert alter type set default 'INSERT';
grant select on audit.pub_insert to chado_ro_role;
grant select, insert on audit.pub_insert to chado_rw_role;

create or replace function audit.public_pub_insert_proc()
returns trigger
as $$
BEGIN
  insert into audit.pub_insert (
      pub_id, title, volumetitle, volume, series_name, issue, pyear, pages, miniref, uniquename, type_id, is_obsolete, publisher, pubplace
  ) values (
      new.pub_id, new.title, new.volumetitle, new.volume, new.series_name, new.issue, new.pyear, new.pages, new.miniref, new.uniquename, new.type_id, new.is_obsolete, new.publisher, new.pubplace
  );
  return new;
END;
$$ language plpgsql;
create trigger pub_audit_insert_tr after insert on public.pub
    for each row execute procedure audit.public_pub_insert_proc();
grant execute on function audit.public_pub_insert_proc() to chado_rw_role;


create table audit.pub_update (
    constraint pub_update_ck check (type = 'UPDATE')
  , old_publisher character varying(255)
  , old_pages character varying(255)
  , old_type_id integer not null
  , old_pyear character varying(255)
  , old_volume character varying(255)
  , old_pubplace character varying(255)
  , old_miniref character varying(255)
  , old_volumetitle text
  , old_is_obsolete boolean
  , old_title text
  , old_uniquename text not null
  , old_series_name character varying(255)
  , old_issue character varying(255)
) inherits (audit.pub);
alter table audit.pub_update alter type set default 'UPDATE';
grant select on audit.pub_update to chado_ro_role;
grant select, insert on audit.pub_update to chado_rw_role;

create or replace function audit.public_pub_update_proc()
returns trigger
as $$
BEGIN
  if old.pub_id <> new.pub_id or old.pub_id is null <> new.pub_id is null then
    raise exception 'If you want to change pub.pub_id (do you really?) then disable the audit trigger pub_audit_update_tr';
  end if;
  insert into audit.pub_update (
      pub_id, title, volumetitle, volume, series_name, issue, pyear, pages, miniref, uniquename, type_id, is_obsolete, publisher, pubplace,
      old_title, old_volumetitle, old_volume, old_series_name, old_issue, old_pyear, old_pages, old_miniref, old_uniquename, old_type_id, old_is_obsolete, old_publisher, old_pubplace
   ) values (
       new.pub_id, new.title, new.volumetitle, new.volume, new.series_name, new.issue, new.pyear, new.pages, new.miniref, new.uniquename, new.type_id, new.is_obsolete, new.publisher, new.pubplace,
       old.title, old.volumetitle, old.volume, old.series_name, old.issue, old.pyear, old.pages, old.miniref, old.uniquename, old.type_id, old.is_obsolete, old.publisher, old.pubplace
   );
  return new;
END;
$$ language plpgsql;
create trigger pub_audit_update_tr after update on public.pub
    for each row execute procedure audit.public_pub_update_proc();
grant execute on function audit.public_pub_update_proc() to chado_rw_role;


create table audit.pub_delete (
    constraint pub_delete_ck check (type = 'DELETE')
) inherits (audit.pub);
alter table audit.pub_delete alter type set default 'DELETE';
grant select on audit.pub_delete to chado_ro_role;
grant select, insert on audit.pub_delete to chado_rw_role;

create or replace function audit.public_pub_delete_proc()
returns trigger
as $$
BEGIN
  insert into audit.pub_delete (
      pub_id, title, volumetitle, volume, series_name, issue, pyear, pages, miniref, uniquename, type_id, is_obsolete, publisher, pubplace
  ) values (
      old.pub_id, old.title, old.volumetitle, old.volume, old.series_name, old.issue, old.pyear, old.pages, old.miniref, old.uniquename, old.type_id, old.is_obsolete, old.publisher, old.pubplace
  );
  return old;
END;
$$ language plpgsql;
create trigger pub_audit_delete_tr after delete on public.pub
    for each row execute procedure audit.public_pub_delete_proc();
grant execute on function audit.public_pub_delete_proc() to chado_rw_role;