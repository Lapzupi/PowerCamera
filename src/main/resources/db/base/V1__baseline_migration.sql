CREATE TABLE IF NOT EXISTS powercamera_cameras (
    id                  VARCHAR(16) NOT NULL,
    alias               VARCHAR(30) NOT NULL,
    total_duration      DOUBLE      NOT NULL,
    return_to_origin    BOOLEAN     NOT NULL DEFAULT 1,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS powercamera_points (
    num                INT AUTO_INCREMENT  NOT NULL,
    camera_id          VARCHAR(16)         NOT NULL,
    world_name         VARCHAR(30)         NOT NULL,
    duration           DOUBLE              NOT NULL,
    type               ENUM('MOVE', 'TELEPORT', 'NONE'),
    easing             ENUM('LINEAR','NONE'),
    x                  INT NOT NULL,
    y                  INT NOT NULL,
    z                  INT NOT NULL,
    yaw                INT NOT NULL,
    pitch              INT NOT NULL,
    PRIMARY KEY (num),
    FOREIGN KEY (camera_id) REFERENCES powercamera_cameras(id)
);

CREATE TABLE IF NOT EXISTS powercamera_players (
    id                 INT AUTO_INCREMENT  NOT NULL,
    uuid               VARCHAR(36)         NOT NULL,
    camera_id          VARCHAR(16)         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (camera_id) REFERENCES powercamera_cameras(id)
);


CREATE TABLE IF NOT EXISTS powercamera_commands_start (
    id                 INT AUTO_INCREMENT NOT NULL,
    point_num          INT                NOT NULL,
    camera_id          VARCHAR(16)        NOT NULL,
    `order`            INT                NOT NULL,
    command            VARCHAR(300)       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (camera_id) REFERENCES powercamera_cameras(id),
    FOREIGN KEY (point_num) REFERENCES powercamera_points(num)
);

CREATE TABLE IF NOT EXISTS powercamera_commands_end (
    id                 INT AUTO_INCREMENT NOT NULL,
    point_num          INT                NOT NULL,
    camera_id          VARCHAR(16)        NOT NULL,
    `order`            INT                NOT NULL,
    command            VARCHAR(300)       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (camera_id) REFERENCES powercamera_cameras(id),
    FOREIGN KEY (point_num) REFERENCES powercamera_points(num)
);