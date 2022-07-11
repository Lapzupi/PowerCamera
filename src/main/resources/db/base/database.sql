CREATE TABLE IF NOT EXISTS `powercamera_cameras` (
    id                  VARCHAR(15) NOT NULL,
    alias               VARCHAR(30) NOT NULL,
    total_duration      DOUBLE      NOT NULL,
    return_to_origin    BOOLEAN     NOT NULL DEFAULT 1,
    PRIMARY KEY(id)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `powercamera_points` (
    num                INT AUTO_INCREMENT NOT NULL,
    camera_id          VARCHAR(15)         NOT NULL,
    PRIMARY KEY (num)
    --FOREIGN KEY (camera_id) REFERENCES powercamera_cameras(id)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `powercamera_players` (
    id                 INT AUTO_INCREMENT NOT NULL,
    uuid               VARCHAR(36)         NOT NULL,
    camera_id          VARCHAR(15)         NOT NULL,
    PRIMARY KEY (id)
    --FOREIGN KEY (camera_id) REFERENCES powercamera_cameras(id)
) DEFAULT CHARSET = utf8mb4;