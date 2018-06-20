package com.migu.schedule;


import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.Node;
import com.migu.schedule.info.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/*
*类名和方法不能修改
 */
public class Schedule {

    // 已经注册到系统的服务节点信息
    private List<Node> nodes;
    
    // 添加的任务信息
    private List<TaskInfo> taskInfos;

    public List<Node> getNodes()
    {
        return nodes;
    }

    public void setNodes(List<Node> nodes)
    {
        this.nodes = nodes;
    }

    public List<TaskInfo> getTaskInfos()
    {
        return taskInfos;
    }

    public void setTaskInfos(List<TaskInfo> taskInfos)
    {
        this.taskInfos = taskInfos;
    }

    /**
     * 功能说明: 
     *         系统初始化，会清空所有数据，包括已经注册到系统的服务节点信息、以及添加的任务信息，全部都被清理。执行该命令后，系统恢复到最初始的状态。

     *  参数说明：
     *           无 

     *   输出说明：
     *          初始化成功，返回E001初始化成功。
     *          未做此题返回 E000方法未实现。
     */
    public int init()
    {
        
        // 清除已经注册到系统的服务节点信息
        if (null != nodes)
        {
            nodes.clear();
        }
        
        // 清除添加的任务信息
        if (null != taskInfos)
        {
            taskInfos.clear();
        }
        
        return ReturnCodeKeys.E001;
    }

    /**
     * 功能说明: 
     *          系统初始化后，服务节点可以通过注册接口注册到本系统。

     * 参数说明：
     *          nodeId 服务节点编号, 每个服务节点全局唯一的标识, 取值范围： 大于0；
    
     * 输出说明：
     *          注册成功，返回E003:服务节点注册成功。
     *          如果服务节点编号小于等于0, 返回E004:服务节点编号非法。
     *          如果服务节点编号已注册, 返回E005:服务节点已注册。
     */
    public int registerNode(int nodeId)
    {
        
        // nodeId 服务节点编号, 每个服务节点全局唯一的标识, 取值范围： 大于0
        // 如果服务节点编号小于等于0, 返回E004:服务节点编号非法
        if (nodeId <= 0)
        {
            return ReturnCodeKeys.E004;
        }
        
        // 如果服务节点编号已注册, 返回E005:服务节点已注册
        if (null != nodes && nodes.size() > 0)
        {
            for (Node node : nodes)
            {
                if (node.getNodeId() == nodeId)
                {
                    return ReturnCodeKeys.E005;
                }
            }
        }
        
        if (null == nodes)
        {
            nodes = new ArrayList<Node>();
        }
        
        Node node = new Node();
        node.setNodeId(nodeId);
        node.setConsumptions(0);
        node.setTaskInfos(new ArrayList<TaskInfo>());
        nodes.add(node);
        // 服务节点注册成功
        return ReturnCodeKeys.E003;
    }

    /**
     * 功能说明: 
     *          1、从系统中删除服务节点；
     *          2、如果该服务节点正运行任务，则将运行的任务移到任务挂起队列中，等待调度程序调度。

     * 参数说明：
     *          nodeId服务节点编号, 每个服务节点全局唯一的标识, 取值范围： 大于0。
    
     * 输出说明：
     *          注销成功，返回E006:服务节点注销成功。
     *          如果服务节点编号小于等于0, 返回E004:服务节点编号非法。
     *          如果服务节点编号未被注册, 返回E007:服务节点不存在。
     */
    public int unregisterNode(int nodeId)
    {
        
        // 如果服务节点编号小于等于0, 返回E004:服务节点编号非法
        if (nodeId <= 0)
        {
            return ReturnCodeKeys.E004;
        }
        
        // 如果服务节点编号未被注册, 返回E007:服务节点不存在。
        if (null == nodes || nodes.size() == 0)
        {
            return ReturnCodeKeys.E007;
        }
        
        for (Node node : nodes)
        {
            
            if (node.getNodeId() == nodeId)
            {
                // 1、从系统中删除服务节点；
                nodes.remove(node);
                
                // 2、如果该服务节点正运行任务，则将运行的任务移到任务挂起队列中，等待调度程序调度。
                if (null != taskInfos && taskInfos.size() > 0)
                {
                    for (TaskInfo task : taskInfos)
                    {
                        if (null != task && task.getNodeId() == nodeId)
                        {
                            task.setNodeId(-1);
                        }
                    }
                }
                
                // 服务节点注销成功
                return ReturnCodeKeys.E006;
            }
        }
        
        // 如果服务节点编号未被注册, 返回E007:服务节点不存在
        return ReturnCodeKeys.E007;
    }

    /**
     * 功能说明: 
     *          将新的任务加到系统的挂起队列中，等待服务调度程序来调度。 

     * 参数说明：
     *          taskId任务编号；取值范围： 大于0。
     *          consumption资源消耗率；
    
     * 输出说明：
     *          添加成功，返回E008任务添加成功。
     *          如果任务编号小于等于0, 返回E009:任务编号非法。
     *          如果相同任务编号任务已经被添加, 返回E010:任务已添加。
     */
    public int addTask(int taskId, int consumption)
    {
        
        // 如果任务编号小于等于0, 返回E009:任务编号非法
        if (taskId <= 0)
        {
            return ReturnCodeKeys.E009;
        }
        
        // 如果相同任务编号任务已经被添加, 返回E010:任务已添加
        if (null != taskInfos && taskInfos.size() > 0)
        {
            for (TaskInfo task : taskInfos)
            {
                if (null != task && task.getTaskId() == taskId)
                {
                    return ReturnCodeKeys.E010;
                }
            }
        }
        
        if (null == taskInfos)
        {
            taskInfos = new ArrayList<TaskInfo>();
        }
        
        TaskInfo task = new TaskInfo();
        task.setNodeId(-1);
        task.setTaskId(taskId);
        task.setConsumption(consumption);
        
        // 将新的任务加到系统的挂起队列中，等待服务调度程序来调度
        taskInfos.add(task);
        
        // 添加成功
        return ReturnCodeKeys.E008;
    }

    /**
     * 功能说明: 
     *          将在挂起队列中的任务 或 运行在服务节点上的任务删除。 

     *参数说明：
     *          taskId任务编号；取值范围： 大于0。

        
     *输出说明：
     *          删除成功，返回E011:任务删除成功。
     *          如果任务编号小于等于0, 返回E009:任务编号非法。
     *          如果指定编号的任务未被添加, 返回E012:任务不存在。
     */
    public int deleteTask(int taskId)
    {
        // 如果任务编号小于等于0, 返回E009:任务编号非法
        if (taskId <= 0)
        {
            return ReturnCodeKeys.E009;
        }
        
        // 将在挂起队列中的任务 或 运行在服务节点上的任务删除
        if (null != taskInfos && taskInfos.size() > 0)
        {
            TaskInfo delTask = null;
            for (TaskInfo task : taskInfos)
            {
                if (null != task && task.getTaskId() == taskId)
                {
                    delTask = task;
                }
            }
            if (null!=delTask)
            {
                taskInfos.remove(delTask);
                // 删除成功
                return ReturnCodeKeys.E011;
            }
        }
        
        // 如果指定编号的任务未被添加, 返回E012:任务不存在
        return ReturnCodeKeys.E012;
    }

    /**
     * 功能说明: 
     *          如果挂起队列中有任务存在，则进行根据上述的任务调度策略，获得最佳迁移方案，进行任务的迁移， 返回调度成功
     *          如果没有挂起的任务，则将运行中的任务则根据上述的任务调度策略，获得最佳迁移方案；
     *          如果在最佳迁移方案中，任意两台不同服务节点上的任务资源总消耗率的差值小于等于调度阈值， 则进行任务的迁移，返回调度成功，
     *          如果在最佳迁移方案中，任意两台不同服务节点上的任务资源总消耗率的差值大于调度阈值，则不做任务的迁移，返回无合适迁移方案
            
     *参数说明：
     *          threshold系统任务调度阈值，取值范围： 大于0；

     *输出说明：
     *          如果调度阈值取值错误，返回E002调度阈值非法。
     *          如果获得最佳迁移方案, 进行了任务的迁移,返回E013: 任务调度成功;
     *          如果所有迁移方案中，总会有任意两台服务器的总消耗率差值大于阈值。则认为没有合适的迁移方案,返回 E014:无合适迁移方案;
     */
    public int scheduleTask(int threshold)
    {
        // 如果调度阈值取值错误，返回E002调度阈值非法
        if (threshold <= 0)
        {
            return ReturnCodeKeys.E002;
        }
        
        // 如果挂起队列中有任务存在，则进行根据上述的任务调度策略，获得最佳迁移方案，进行任务的迁移， 返回调度成功
        if (null != taskInfos && taskInfos.size() > 0 && null != nodes
            && nodes.size() > 0)
        {
            /*
             * ※ 约束： 总消耗率差值最小的迁移方案（迁移后，所有的物理服务器的总消耗率相同，或者两两消耗率差值最小）
             * 尽量把任务平均分配到各个服务器上。（这里指任务数量，也就是两两服务器上的任务总数差值最小）
             * 如果存在资源消耗率相同的任务，则优先将编号小的任务迁移到编号小的服务器上；
             * 如果迁移后，有任意两台服务器的总消耗率相同，则应保证编号小的服务器的运行任务总数量少；
             * 如果迁移后，所有的物理服务器的总消耗率不相同，保证编号大的服务器的总消耗大于编号小的服务器的总消耗。
             * 如果迁移后，满足以上要求的方案有多个，则应选择编号小的服务器上的任务编号升序序列最小。
             * (假设有四个任务编号：1、2、3、4，分配到2台服务器, 满足以上要求有两个方案, 方案1 : 1,2任务在服务器1 ;
             * 3,4任务在服务器2 方案2 : 2,3任务在服务器1 ; 1,4任务在服务器2 则 方案1 的 服务器1 的升序排列 12
             * <方案2 的 服务器1 的升序排列 23)
             */
            
            // 任务均分
            int taskSize = taskInfos.size();
            int nodeSize = nodes.size();
            int pingjun = (taskSize / nodeSize)+1;
            
            queryTaskStatus(taskInfos);
            
            for (int i = 0; i < taskInfos.size() - 1; i++)
            {
                int temp = 0;
                for (int j = taskInfos.size() - 1; j > i; j--)
                {
                    TaskInfo jT = taskInfos.get(j);
                    TaskInfo kT = taskInfos.get(j - 1);
                    // 升序
                    if (jT.getTaskId() > kT.getTaskId())
                    {
                        
                    }
                }
            }
            
        }
        
        // 如果获得最佳迁移方案, 进行了任务的迁移,返回E013: 任务调度成功
        return ReturnCodeKeys.E013;
    }

    /**
     * 功能说明: 
     *          查询获得所有已添加任务的任务状态,  以任务列表方式返回。
            
     *参数说明：
     *          Tasks 保存所有任务状态列表；要求按照任务编号升序排列,
     *          如果该任务处于挂起队列中, 所属的服务编号为-1;
     *          在保存查询结果之前,要求将列表清空.
     *输出说明：
     *          未做此题返回 E000方法未实现。
     *          如果查询结果参数tasks为null，返回E016:参数列表非法
     *          如果查询成功, 返回E015: 查询任务状态成功;查询结果从参数Tasks返回。
     */
    public int queryTaskStatus(List<TaskInfo> tasks)
    {
        // 如果查询结果参数tasks为null，返回E016:参数列表非法
        if (null == taskInfos)
        {
            return ReturnCodeKeys.E016;
        }
        
        // 在保存查询结果之前,要求将列表清空
        if (null == tasks)
        {
            tasks = new ArrayList<TaskInfo>();
        }
        else
        {
            tasks.clear();
        }
        
        for (int i = 0; i < taskInfos.size() - 1; i++)
        { 
            for (int j = taskInfos.size() - 1; j > i; j--)
            {
                TaskInfo jT = taskInfos.get(j);
                TaskInfo kT = taskInfos.get(j - 1);
                // 升序
                if (jT.getTaskId() < kT.getTaskId())
                {
                    tasks.set(j, kT);
                    tasks.set(j - 1, jT);
                }
            }
        }
        
        // 如果查询成功, 返回E015: 查询任务状态成功;查询结果从参数Tasks返回
        return ReturnCodeKeys.E015;
    }

}
