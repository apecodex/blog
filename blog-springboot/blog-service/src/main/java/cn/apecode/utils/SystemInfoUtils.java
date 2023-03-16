package cn.apecode.utils;

import cn.apecode.dto.DiskInfoDto;
import cn.apecode.dto.MemoryInfoDto;
import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 系统信息
 * @author: apecode
 * @date: 2022-08-24 01:21
 **/
public class SystemInfoUtils {

    public static MemoryInfoDto getMemory() {
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 获取内存总容量
        long totalMemorySize = mem.getTotalPhysicalMemorySize();
        // 获取可用内存容量
        long freeMemorySize = mem.getFreePhysicalMemorySize();
        return MemoryInfoDto.builder()
                .total(formatByte(totalMemorySize))
                .available(formatByte(freeMemorySize))
                .used(formatByte(totalMemorySize - freeMemorySize))
                .ute(new DecimalFormat("#.##%").format((totalMemorySize - freeMemorySize) * 1.0 / totalMemorySize))
                .build();
    }

    public static List<DiskInfoDto> getDiskInfo() {
        File[] roots = File.listRoots();// 获取磁盘分区列表
        List<DiskInfoDto> diskInfoDtoList = new ArrayList<>();
        for (File file : roots) {
            long free = file.getFreeSpace();
            long total = file.getTotalSpace();
            long use = total - free;
            DiskInfoDto diskInfoDto = DiskInfoDto.builder()
                    .path(file.getPath())
                    .free(formatByte(free))
                    .use(formatByte(use))
                    .total(formatByte(total))
                    .build();
            diskInfoDtoList.add(diskInfoDto);
        }
        return diskInfoDtoList;
    }

    public static String formatByte(long byteNumber) {
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }
}
