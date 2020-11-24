-- U-things
CREATE TABLE [message] (
	[id] bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[moteeui] nvarchar(254),
	[dir] nvarchar(254),
	[seqno] int,
	[port] int,
	[payload] nvarchar(254),
	[created_at] datetime,
	[updated_at] datetime,
) ON [PRIMARY];

CREATE TABLE [motee] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[moteeui] nvarchar(254) NOT NULL,
	[type] tinyint
	[location] nvarchar(254),
	[created_at] datetime,
	[updated_at] datetime
) ON [PRIMARY];

CREATE TABLE [location] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[name] nvarchar(254) NOT NULL,
	[image] nvarchar(254)
) ON [PRIMARY];

-- Machine

CREATE TABLE [machine] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[name] nvarchar(254) NOT NULL,
	[json] ntext
) ON [PRIMARY];

CREATE TABLE [process] (
	[id] bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[machine_id] int NOT NULL,
	[time_start] datetime NOT NULL,
	[time_end] datetime NOT NULL,
	[time_run] bigint NOT NULL
) ON [PRIMARY];

CREATE TABLE [step] (
	[id] bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[process_id] bigint NOT NULL,
	[time_start] datetime,
	[time_end] datetime,
	[time_run] bigint,
	[name] nvarchar(254) NOT NULL
) ON [PRIMARY];

CREATE TABLE [sensor] (
	[id] bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[process_id] bigint NOT NULL,
	[step_id] bigint NOT NULL,
	[name] nvarchar(254) NOT NULL,
	[type] tinyint NOT NULL,
	[time_start] datetime,
	[time_end] datetime,
	[data] nvarchar(254)
) ON [PRIMARY];

CREATE TABLE [error] (
	[id] bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[process_id] bigint,
	[step_id] bigint,
	[error_code] nvarchar(254),
	[error_count] int
) ON [PRIMARY];

-- postman

CREATE TABLE [postman_order] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[allow_forward] tinyint,
	[message] text,
	[data] text,
	[image] text,
	[created_at] datetime2(7),
	[updated_at] datetime2(7)
) ON [PRIMARY];

CREATE TABLE [postman_sign_history] (
	[id] bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[order_id] int,
	[serial] tinyint,
	[sign_user] nvarchar(254),
	[status] nvarchar(254),
	[sign_note] text,
	[sign_time] datetime2(7),
	[created_at] datetime2(7),
	[updated_at] datetime2(7)
) ON [PRIMARY];

-- Test

CREATE TABLE [test_factory_meta] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[factory] nvarchar(254)
) ON [PRIMARY];

CREATE TABLE [test_model_meta] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[factory] nvarchar(254),
	[model_name] nvarchar(254)
) ON [PRIMARY];

CREATE TABLE [test_group_meta] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[factory] nvarchar(254),
	[group_name] nvarchar(254),
	[model_name] nvarchar(254)
) ON [PRIMARY];

CREATE TABLE [test_station_meta] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[factory] nvarchar(254),
	[station_name] nvarchar(254),
	[group_name] nvarchar(254),
	[model_name] nvarchar(254)
) ON [PRIMARY];

CREATE TABLE [test_group] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[factory] nvarchar(254),
	[model_name] nvarchar(254),
	[group_name] nvarchar(254),
	[start_date] datetime2(7),
	[end_date] datetime2(7),
	[wip] int,
	[first_fail] int,
	[second_fail] int,
	[pass] int,
	[fail] int,
	[retest_rate] float
) ON [PRIMARY];

CREATE TABLE [test_station] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[factory] nvarchar(254),
	[model_name] nvarchar(254),
	[group_name] nvarchar(254),
	[station_name] nvarchar(254),
	[start_date] datetime2(7),
	[end_date] datetime2(7),
	[wip] int,
	[first_fail] int,
	[second_fail] int,
	[pass] int,
	[fail] int,
	[retest_rate] float
) ON [PRIMARY];

CREATE TABLE [test_error] (
	[id] int NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[factory] nvarchar(254),
	[model_name] nvarchar(254),
	[group_name] nvarchar(254),
	[station_name] nvarchar(254),
	[start_date] datetime2(7),
	[end_date] datetime2(7),
	[error_code] nvarchar(254),
	[test_fail] int,
	[fail] int,
	[dppm] int
) ON [PRIMARY];

CREATE TABLE [test_notify] (
	[id] bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	[factory] nvarchar(254),
	[model_name] nvarchar(254),
	[group_name] nvarchar(254),
	[station_name] nvarchar(254),
	[start_date] datetime2(7),
	[end_date] datetime2(7),
	[wip] int,
    [first_fail] int,
    [second_fail] int,
    [pass] int,
    [fail] int,
    [message] text,
    [status] tinyint,
	[created_at] datetime2(7),
	[updated_at] datetime2(7)
) ON [PRIMARY];