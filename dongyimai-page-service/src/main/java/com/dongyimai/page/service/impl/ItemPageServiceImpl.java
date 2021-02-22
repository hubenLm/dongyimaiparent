package com.dongyimai.page.service.impl;

import com.dongyimai.mapper.TbGoodsDescMapper;
import com.dongyimai.mapper.TbGoodsMapper;
import com.dongyimai.mapper.TbItemCatMapper;
import com.dongyimai.mapper.TbItemMapper;
import com.dongyimai.page.service.ItemPageService;
import com.dongyimai.pojo.TbGoods;
import com.dongyimai.pojo.TbGoodsDesc;
import com.dongyimai.pojo.TbItem;
import com.dongyimai.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;

@Service
public class ItemPageServiceImpl implements ItemPageService {
    private String pagedir = "C:\\Users\\Administrator\\IdeaProjects\\dongyimaiparent\\dongyimai-page-web\\src\\main\\webapp\\";

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public boolean genItemHtml(Long goodsId) {
        //获取配置对象
        try {
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");

            //去商品数据
            HashMap map = new HashMap();
            //1、加载商品表数据
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            map.put("goods",goods);

            //2、加载商品扩展表数据
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            map.put("goodsDesc",goodsDesc);

            //3、商品分类
            String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            map.put("itemCat1",itemCat1);
            map.put("itemCat2",itemCat2);
            map.put("itemCat3",itemCat3);

            //4、SKU列表
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo("1");//状态为有效
            criteria.andGoodsIdEqualTo(goodsId);//指定SPU ID
            example.setOrderByClause("is_default desc");//按照状态降序，保证第一个为默认
            List<TbItem> itemList = itemMapper.selectByExample(example);
            map.put("itemList",itemList);

            //创建文件流
            Writer out = new FileWriter(pagedir + goodsId + ".html");
            template.process(map,out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteItemHtml(Long[] goodsIds) {
        try {
            for (Long goodsId : goodsIds) {
                new File(pagedir+goodsIds+".html").delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
