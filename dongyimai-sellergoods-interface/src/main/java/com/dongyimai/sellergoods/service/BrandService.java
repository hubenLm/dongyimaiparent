package com.dongyimai.sellergoods.service;

import com.dongyimai.entity.PageResult;
import com.dongyimai.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    public List<TbBrand> findAll();

    public PageResult findPages(int pageNum,int pageSize);

    public void save(TbBrand tbBrand);

    public TbBrand findOne(Long bid);

    public void update(TbBrand tbBrand);

    public void delete(Long [] ids);

    public PageResult search(TbBrand tbBrand,int page,int rows);

    List<Map> selectOptionList();
}
