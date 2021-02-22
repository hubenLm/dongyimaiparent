//商品详情页（控制层）
app.controller('itemController',function($scope){
	
	//添加商品到购物车
	$scope.addToCart=function(){
		alert('skuid:'+$scope.sku.id);				
	}
	
	//查询SKU
	searchSku=function(){
		for(var i=0;i<skuList.length;i++ ){
			if( matchObject(skuList[i].spec ,$scope.specificationItems ) ){
				$scope.sku=skuList[i];
				return ;
			}			
		}	
		$scope.sku={id:0,title:'--------',price:0};//如果没有匹配的		
	}
	
	//匹配两个对象
	//{"网络":"移动3G","机身内存":"32G"} map1
	//{"网络":"移动3G","机身内存":"32G"} map2
	matchObject=function(map1,map2){	
		for(var k in map1){
			if(map1[k]!=map2[k]){
				return false;
			}			
		}
		for(var k in map2){
			if(map2[k]!=map1[k]){
				return false;
			}			
		}
		return true;		
	}
	
	//加载默认SKU
	$scope.loadSku=function(){
		$scope.sku=skuList[0];		
		$scope.specificationItems= JSON.parse(JSON.stringify($scope.sku.spec)) ;
	}
	
	$scope.specificationItems={};//记录用户选择的规格
	//用户选择规格
	$scope.selectSpecification=function(name,value){	
		$scope.specificationItems[name]=value;
		searchSku();//读取sku
	}	
	//网络：5寸
	//判断某规格选项是否被用户选中
	$scope.isSelected=function(name,value){
		if($scope.specificationItems[name]==value){
			return true;
		}else{
			return false;
		}		
	}
	
	//数量操作
	$scope.addNum=function(x){
		$scope.num=$scope.num+x;
		if($scope.num<1){
			$scope.num=1;
			alert("最小购买数量1个");
		}
		if($scope.num>200){
			$scope.num=200;
			alert("最大购买数量200个");
		}	
	}

	
});