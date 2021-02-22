package com.dongyimai.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dongyimai.entity.PageResult;
import com.dongyimai.mapper.TbBrandMapper;
import com.dongyimai.pojo.TbBrand;
import com.dongyimai.pojo.TbBrandExample;
import com.dongyimai.sellergoods.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPages(int pageNum, int pageSize) {
        //封装分页工具
        PageHelper.startPage(pageNum,pageSize);
        //再查询
        Page<TbBrand> page =(Page<TbBrand>) tbBrandMapper.selectByExample(null);
        //page.getResult() 两个结果封装到一个对象当中
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void save(TbBrand tbBrand) {
        tbBrandMapper.insert(tbBrand);
    }

    @Override
    public TbBrand findOne(Long bid) {
        return tbBrandMapper.selectByPrimaryKey(bid);
    }

    @Override
    public void update(TbBrand tbBrand) {
         tbBrandMapper.updateByPrimaryKey(tbBrand);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult search(TbBrand tbBrand, int page, int rows) {
        PageHelper.startPage(page, rows);
        TbBrandExample example=new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();		//建立查询条件
        if(tbBrand!=null){
            if(tbBrand.getName()!=null && tbBrand.getName().length()>0){
                criteria.andNameLike("%"+tbBrand.getName()+"%");
            }
            if(tbBrand.getFirstChar()!=null && tbBrand.getFirstChar().length()>0){
                criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
            }
        }
        Page<TbBrand> brandPage= (Page<TbBrand>)tbBrandMapper.selectByExample(example);
        return new PageResult(brandPage.getTotal(), brandPage.getResult());

    }

    @Override
    public List<Map> selectOptionList() {
        return tbBrandMapper.selectOptionList();
    }

}
