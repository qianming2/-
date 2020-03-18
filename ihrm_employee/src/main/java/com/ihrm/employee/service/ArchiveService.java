package com.ihrm.employee.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.util.IdWorker;
import com.ihrm.domain.employee.EmployeeArchive;
import com.ihrm.employee.dao.ArchiveDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.Predicate;

@Service
public class ArchiveService extends BaseService {
    @Autowired
    private ArchiveDao archiveDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 保存员工归档
     * */
    public void save(EmployeeArchive archive){
        //生成id
        archive.setId(idWorker.nextId()+"");
        //创建日期
        archive.setCreateTime(new Date());
        archiveDao.save(archive);
    }

    /**
     * 查找最后
     * */
    public EmployeeArchive findLast(String company_id,String month){
        EmployeeArchive archive = archiveDao.findByLast(company_id, month);
        return archive;
    }
    /**
     * 查找全部并分页
     * */
    public List<EmployeeArchive> findAll(Integer page, Integer pagesize, String year, String company_id){
        int index=(page-1)*pagesize;
        return archiveDao.findAllData(company_id,year+"%",index,pagesize);
    }

    /**
     * 统计数量
     * */
    public Long countAll(String year, String company_id){
        return archiveDao.countAllData(company_id,year+"%");
    }

    /**
     * 根据条件查询
     * */
    public Page<EmployeeArchive> findSearch(Map<String,Object> map,int page,int size){
        return archiveDao.findAll(createSpecification(map), PageRequest.of(page-1,size));
    }


    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<EmployeeArchive> createSpecification(Map searchMap) {
        return new Specification<EmployeeArchive>() {
            @Override
            public Predicate toPredicate(Root<EmployeeArchive> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                // 企业id
                if (searchMap.get("company_Id")!=null && !"".equals(searchMap.get("company_Id"))) {
                    predicateList.add(cb.like(root.get("companyId").as(String.class), (String)searchMap.get("companyId")));
                }
                if (searchMap.get("year")!=null && !"".equals(searchMap.get("year"))) {
                    predicateList.add(cb.like(root.get("mouth").as(String.class), (String)searchMap.get("year")));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
