app.service('brandService',function ($http) {

    this.selectOptionList = function () {
        return $http.get('../brand/selectOptionList.do');
    }

    this.save = function (methodStr,pojo) {
        return $http.post('../brand/'+methodStr+'.do',pojo);
    }

    this.search = function (page,rows,searchEntity) {
        return $http.post('../brand/search.do?page='+page+"&rows="+rows,searchEntity);
    }

    this.dele = function (ids) {
        return $http.get('../brand/dele.do?ids='+ids);
    }

    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?bid='+id);
    }

    this.finaAll = function () {
        return $http.get('../brand/findAll.do');
    }

    this.findPage = function (page,rows) {
        return $http.get('../brand/findPage.do?page='+page+'&rows='+rows);
    }

});