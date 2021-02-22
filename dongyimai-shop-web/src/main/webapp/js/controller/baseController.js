app.controller('baseController',function ($scope) {

    //定义复选框id数组
    $scope.selectIds = [];

    //勾选复选框的方法
    $scope.updateSelection  = function($event,id){
        //勾选
        if($event.target.checked){
            $scope.selectIds.push(id);
            //不勾选
        }else{
            //获取该id所在下标
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx,1);
        }
        // alert($scope.selectIds);
    }

    $scope.searchEntity = {};

    $scope.reloadList=function(){
        //切换页码
        //$scope.findPage( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };

    //提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
    $scope.jsonToString=function(jsonString,key){
        var json=JSON.parse(jsonString);//将json字符串转换为json对象
        var value="";
        for(var i=0;i<json.length;i++){
            if(i>0){
                value+=","
            }
            value+=json[i][key];
        }
        return value;
    }

})