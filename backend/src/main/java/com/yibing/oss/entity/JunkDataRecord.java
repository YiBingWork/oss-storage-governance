package com.yibing.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 无用数据识别记录
 */
@Data
@TableName("junk_data_record")
public class JunkDataRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bucketId;

    /** OSS 对象 Key */
    private String objectKey;

    /** 文件大小 (bytes) */
    private Long sizeBytes;

    /** 无用原因: EXPIRED / NO_ACCESS / TEMP_FILE / DUPLICATE */
    private String reason;

    /** 最后访问时间 */
    private LocalDateTime lastAccessTime;

    /** 处理状态: PENDING / DELETED / RETAINED */
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
