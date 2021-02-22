 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承

	$scope.status=['未审核','已审核','审核未通过','关闭'];//商品状态

	$scope.entity={
		goods:{},
		goodsDesc:{
			itemImages:[],
			specificationItems:[]
		},
		itemList:[]
	};

	$scope.itemCatList=[];//商品分类列表
	//加载商品分类列表
	$scope.findItemCatList = function(){
		itemCatService.findAll().success(function (response) {
			for (var i=0;i<response.length;i++){
				//id做下标，名称作为内容
				$scope.itemCatList[response[i].id] = response[i].name;
			}
		})
	}


	//创建SKU列表
	$scope.createItemList = function(){
		//定义数组
		$scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0' }];//初始
		//勾选SKU  获取勾选过的规格对象
		var items = $scope.entity.goodsDesc.specificationItems;
		for(var i=0;i<items.length;i++){
			$scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
	}
	//添加列值
	addColumn=function(list,columnName,conlumnValues){
		var newList=[];//新的集合
		for (var i=0;i<list.length;i++){
			var oldRow = list[i];  //["移动3G","移动4G"]
			for (var j=0;j<conlumnValues.length;j++){
				var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆
				newRow.spec[columnName] = conlumnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}


	//选择规格
	$scope.updateSpecAttribute = function($event, name, value) {
		//1、调用查询已选择的规格方法 获取此时用户选择过的规格数组
		var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, 'attributeName',name);
		if (object != null) {
			//2、判断复选框是否为勾选状态
			//2.1 若为勾选则向 attributeValue 数组 中添加一个 值
			if ($event.target.checked) {
				object.attributeValue.push(value);
			} else {
				// 取消勾选
				object.attributeValue.splice(object.attributeValue.indexOf(value ) ,1);//移除选项
				// 如果选项都取消了，将此条记录移除
				if (object.attributeValue.length == 0) {
					$scope.entity.goodsDesc.specificationItems.splice(
						$scope.entity.goodsDesc.specificationItems
							.indexOf(object), 1);
				}
			}
		} else {
			//如果用户没有勾选过该规格对象
			//则 向其中 添加一个对象 {'attributeName':specName,'attributeValue':[specValue]}
			$scope.entity.goodsDesc.specificationItems.push({
				"attributeName" : name,
				"attributeValue" : [ value ]
			});
		}
	}

	//从集合中按照key查询对象
	//做一个查询的方法 根据用户所选择的规格名称 判断用户之前是否选择过该规格
	//a) 未选择过 返回 null
	//b) 选择过 返回 选择过的 数组集合 --> 去判断 该选择值 是否选择过
	$scope.searchObjectByKey = function(list,key,keyValue){
		for(var i=0;i<list.length;i++){
			if(list[i][key]==keyValue){
				return list[i];
			}
		}
		return null;
	}

	//模板ID选择后，更新品牌列表
	// $scope.$watch("entity.goods.typeTemplateId",function (newValue,oldValue) {
	// 	typeTemplateService.findOne(newValue).success(function (response) {
	// 		$scope.typeTemplate = response;//获取类型模板
	// 		$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);//品牌列表
	// 	})
	// })
	//模板ID选择后，更新品牌列表
	$scope.$watch("entity.goods.typeTemplateId",function (newValue,oldValue) {
		typeTemplateService.findOne(newValue).success(function (response) {
			$scope.typeTemplate = response;//获取类型模板
			$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);//品牌列表
			//如果没有ID，则加载模板中的扩展属性
			if($location.search()["id"]==null){
				$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
			}
		});
		//查询规格列表
		typeTemplateService.findSpecList(newValue).success(function (response) {
			$scope.specList=response;
		})
	})

	//读取一级目录
	$scope.selectItemCat1List = function(){
		itemCatService.findByParentId(0).success(function (response) {
			$scope.itemCat1List = response;
		})
	}

	//读取二级分类
	$scope.$watch("entity.goods.category1Id",function (newValue,oldValue) {
		itemCatService.findByParentId(newValue).success(function (response) {
			$scope.itemCat2List = response;
		})
	})

	//读取三级分类
	$scope.$watch("entity.goods.category2Id",function (newValue,oldValue) {
		itemCatService.findByParentId(newValue).success(function (response) {
			$scope.itemCat3List = response;
		})
	})

	//三级分类后，读取模板ID
	$scope.$watch("entity.goods.category3Id",function (newValue,oldValue) {
		itemCatService.findOne(newValue).success(function (response) {
			$scope.entity.goods.typeTemplateId = response.typeId;//商品分类的类型ID赋给模板ID
		})
	})


	//添加图片列表
	$scope.addImgList = function(imageEntity){
		$scope.entity.goodsDesc.itemImages.push(imageEntity);
	}

	$scope.removeImg = function(image){
	    var ids = $scope.entity.goodsDesc.itemImages.indexOf(image);
	    $scope.entity.goodsDesc.itemImages.splice(ids,1);
    }

	//上传图片
	$scope.uploadFile = function(){
		uploadService.uploadFile().success(function (response) {
			if(response.success){
				$scope.image_entity.url = response.message;
			}else {
				alert(response.message);
			}
		}).error(function () {
			alert("上传发生错误");
		})
	}

	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
		var id = $location.search()["id"];//获取参数
		if(id==null){
			return;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				//向富文本编辑器添加商品介绍
				editor.html($scope.entity.goodsDesc.introduction);
				//显示图片列表  把json类型的图片转换为字符串类型
				$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
				//显示扩展属性
				$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
				//规格
				$scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);
				//sku列表规格列转换  对sku列表中的 每一个对象的spec 进行json格式转换
				for(var i=0;i<$scope.entity.itemList.length;i++){
					$scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
				}
			}
		);				
	}

	//根据规格名称和选项名称返回是否被勾选
	$scope.checkAttributeValue = function(specName,optionName){
		//已经选择过的规格列表
		var items = $scope.entity.goodsDesc.specificationItems;
		//searchObjectByKey方法根据用户所选择的规格名称 判断用户之前是否选择过该规格
		var object = $scope.searchObjectByKey(items,"attributeName",specName);
		//之前没有选择过该ID
		if(object==null){
			return false;
		}else {
			//之前选择过该规格
			//是否选择过该属性值
			if (object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else {
				return false;
			}
		}
	}
	
	//保存 
	$scope.save=function(){

		//绑定商品介绍的参数
		$scope.entity.goodsDesc.introduction = editor.html();

		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	//$scope.reloadList();//重新加载
					/*alert("保存成功");
					$scope.entity={}
					editor.html('');//清空富文本编辑器*/
					location.href="goods.html";//跳转到商品列表页
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
});	