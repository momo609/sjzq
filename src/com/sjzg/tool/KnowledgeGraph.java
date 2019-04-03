package com.sjzg.tool;

public interface  KnowledgeGraph {
	static final  int[][] edge ={{1,1,0,0,0,0,0,0,0,0,0},
        {0,1,1,0,0,0,0,0,0,0,0},
        {0,0,1,1,0,0,0,0,0,0,0},
        {0,0,0,1,0,0,1,0,0,0,0},
        {0,0,0,0,1,0,0,0,0,0,0},
        {0,0,1,0,0,1,0,0,0,0,0},
        {0,0,0,0,0,0,1,0,1,0,0},
        {0,0,0,0,0,1,0,1,0,1,0},
        {0,0,0,0,1,0,0,0,1,0,1},
        {0,1,0,0,0,0,0,0,0,1,0},
        {0,0,0,0,0,0,0,0,0,0,1}};
	static final  String concepts[]={"1NF","2NF","3NF","BCNF","主属性","传递函数依赖","决定因素","函数依赖","码","部分函数依赖","非主属性"};
}
