//购物车控制层
app.controller('cartController',function($scope,cartService){

    //保存订单
    $scope.submitOrder = function(){
        $scope.order.receiverAreaName = $scope.addrsss.addrsss;//地址
        $scope.order.receiverMobile = $scope.addrsss.mobile;//手机
        $scope.order.receiver = $scope.addrsss.contact;//联系人
        cartService.submitOrder($scope.order).success(function (response) {
            if(response.success){
                //页面跳转
                if($scope.order.paymentType=='1'){//如果是微信支付，跳转到支付页面
                    location.href = "paysuccess.html";
                }
            }else {
                alert(response.message);//也跳转到提示页面
            }
        })
    }


    $scope.order = {paymentType:'1'};

    //选择支付方式
    $scope.selectPayType = function(type){
        $scope.order.paymentType=type;
    }

    //选择地址
    $scope.selectAddress=function(address){
        $scope.addrsss = address;
    }

    //判断是否是当前选中的地址
    $scope.isSelectedAddress = function(address){
        if(address ==$scope.addrsss){
            return true;
        }else {
            return false;
        }
    }


    //获取地址列表
    $scope.findAddressList = function(){
        cartService.findAddressList().success(function (response) {
            $scope.addressList = response;

            //设置默认地址
            for (var i=0;i<$scope.addressList.length;i++){
                if($scope.addressList[i].isDefault=='1'){
                    $scope.addrsss=$scope.addressList[i];
                    break;
                }
            }
        })
    }

    //查询购物车列表
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
                $scope.totalValue = cartService.sum($scope.cartList);//求合计体
            }
        );
    }

    //添加商品到购物车
    $scope.addGoodsToCartList = function (itemId, num) {
        cartService.addGoodsToCartList(itemId,num).success(function (response) {
            if(response.success){
                $scope.findCartList();//刷新列表
            }else {
                alert(response.message);//弹出错误提示
            }
        })
    }
});
