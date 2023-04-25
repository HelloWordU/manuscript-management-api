package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.CategoryHotWord;
import com.rz.manuscript.entity.ManuscriptType;
import com.rz.manuscript.pojo.vo.CategoryHotWordVo;
import com.rz.manuscript.service.ICategoryHotWordService;
import com.rz.manuscript.service.IManuscriptTypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/manuscriptType")
@Slf4j
@Api(tags = "稿件类型")
public class ManuscriptTypeController {

    @Resource
    private IManuscriptTypeService iManuscriptTypeService;

    @GetMapping("/get")
    public ResultEntityList<ManuscriptType> get() {
        LambdaQueryWrapper<ManuscriptType> wrapper = new LambdaQueryWrapper<>();
        List<ManuscriptType> res = iManuscriptTypeService.list(wrapper);
        return new ResultEntityList(200, res, "获取成功");
    }

    @PostMapping("/save")
    public ResultEntity<Boolean> save(@RequestBody ManuscriptType data) {

        try {
            iManuscriptTypeService.saveOrUpdate(data);
            return new ResultEntity(200, true, "保存成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 保存失败", e);
            return new ResultEntity(0, false, "保存失败");
        }

    }

    @PostMapping("/delete")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        try {
            iManuscriptTypeService.removeById(id);
            return new ResultEntity(200, true, "删除成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 删除失败", e);
            return new ResultEntity(0, false, "删除失败");
        }

    }

}
