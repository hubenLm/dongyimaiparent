app.controller('brandController',function ($scope,$controller,brandService) {

    $controller('baseController',{$scope:$scope});//继承

    $scope.search = function(page,rows){
        brandService.search(page,rows,$scope.searchEntity).success(
            function (response) {
                $scope.list=response.rows;	//显示当前页的数据
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    $scope.dele = function(){
        brandService.dele($scope.selectIds).success(
            function (response) {
                if(response.success){
                    $scope.reloadList();
                }else{
                    alert(response.message);
                }
            }
        );
    }




    //根据主键查询
    $scope.findOne = function(id){
        brandService.findOne(id).success(
            function (response) {
                //由于 新增和修改用同一个编辑窗口 所以此处必须和展现对象一致 也就是 pojo
                $scope.pojo = response;
            }
        );
    }
    //保存
    $scope.save = function(){

        var methodStr = "save";

        if($scope.pojo.id != null){
            methodStr = "update";
        }

        brandService.save(methodStr,$scope.pojo).success(
            function (response) {
                if(response.success){
                    //保存成功 则刷新页面 展现新增数据
                    $scope.reloadList();
                }else{
                    alert(response.message);
                }
            }
        );

    }

    //调用后台品牌列表的方法
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //重新加载列表 数据


    //分页
    $scope.findPage=function(page,rows){
        brandService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;	//显示当前页的数据
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

});