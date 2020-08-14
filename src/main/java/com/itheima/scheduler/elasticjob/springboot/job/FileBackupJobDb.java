package com.itheima.scheduler.elasticjob.springboot.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.itheima.scheduler.elasticjob.springboot.model.FileCustom;
import com.itheima.scheduler.elasticjob.springboot.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 文件备份任务
 * @author Administrator
 * @version 1.0
 **/
@Component
public class FileBackupJobDb implements SimpleJob {

    //每次任务执行要备份文件的数量
    private final int FETCH_SIZE = 1;

    @Autowired
    FileService fileService;

    //任务执行代码逻辑
    @Override
    public void execute(ShardingContext shardingContext) {

        //分片参数，（0=text,1=image,2=radio,3=vedio，参数就是text、image...）
        String jobParameter = shardingContext.getShardingParameter();

        //1.获取未备份文件
        List<FileCustom> unBackupFiles =
                fileService.fetchUnBackupFiles(jobParameter, FETCH_SIZE);

        System.out.println(String.format("Time: %s | 线程 %d | 分片 %s | 已获取文件数据 %d 条"
                ,new SimpleDateFormat("HH:mm:ss").format(new Date())
                ,Thread.currentThread().getId()
                ,jobParameter
                ,unBackupFiles.size()));

        //2.执行文件备份操作
        fileService.backupFiles(unBackupFiles);
    }

}
