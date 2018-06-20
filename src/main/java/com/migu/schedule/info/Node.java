package com.migu.schedule.info;

import java.util.List;

/**
 * 物理服务器信息类。
 *
 * @author
 * @version
 */
public class Node
{
    private int nodeId;
    
    // 总资源消耗率
    private int consumptions;
    
    private List<TaskInfo> taskInfos;
    
    public int getNodeId()
    {
        return nodeId;
    }
    
    public void setNodeId(int nodeId)
    {
        this.nodeId = nodeId;
    }
    
    public int getConsumptions()
    {
        return consumptions;
    }
    
    public void setConsumptions(int consumption)
    {
        this.consumptions += consumption;
    }
    
    public List<TaskInfo> getTaskInfos()
    {
        return taskInfos;
    }
    
    public void setTaskInfos(List<TaskInfo> taskInfos)
    {
        this.taskInfos = taskInfos;
    }
    
    @Override
    public String toString()
    {
        return "TaskInfo [nodeId=" + nodeId + ", consumption=" + consumptions
            + ", taskInfos=" + taskInfos + "]";
    }
}
