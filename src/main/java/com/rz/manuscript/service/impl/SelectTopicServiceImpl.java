package com.rz.manuscript.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rz.manuscript.client.CollectClient;
import com.rz.manuscript.common.EasyExcelUtil;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.entity.SelectTopic;
import com.rz.manuscript.entity.WriteSelectedTopic;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.mapper.SelectTopicMapper;
import com.rz.manuscript.mapper.WriteSelectedTopicMapper;
import com.rz.manuscript.pojo.request.CollectAnalysisRequest;
import com.rz.manuscript.pojo.vo.CollectResultVo;
import com.rz.manuscript.pojo.vo.SelectTopicGetListRequest;
import com.rz.manuscript.pojo.vo.SelectTopicUploadVo;
import com.rz.manuscript.pojo.vo.SelectTopicVo;
import com.rz.manuscript.service.ISelectTopicService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelectTopicServiceImpl extends ServiceImpl<SelectTopicMapper, SelectTopic> implements ISelectTopicService {
    @Resource
    private SelectTopicMapper selectTopicMapper;

    @Resource
    private WriteSelectedTopicMapper writeSelectTopicMapper;

    @Resource
    private CollectClient collectClient;


    @Override
    public List<SelectTopicVo> getList(SelectTopicGetListRequest request) {
        setRequestPage(request);
        return selectTopicMapper.getList(request);
    }

    @Override
    public ResultEntity<Boolean> addToWrite(Integer id, Integer projectId,Integer userId) {
        try {
            //获取当前选题数据
            SelectTopic selectTopic = selectTopicMapper.selectById(id);
            if (selectTopic == null)
                throw new RZException("无效的热文选题编号");
            selectTopic.setIsSelected(true);

            String content = "";
            if (selectTopic.getIsDownload() == null || !selectTopic.getIsDownload()) {
                CollectAnalysisRequest request = new CollectAnalysisRequest();
                request.setUrl(selectTopic.getUrl());
                CollectResultVo collectResultVo = new CollectResultVo();
                Integer contentLength = 0;
                try {
                    String analysisInfo = collectClient.analysis(request);
                    collectResultVo = JSON.parseObject(analysisInfo,CollectResultVo.class);
                    if (!collectResultVo.getContent().isEmpty()) {
                        for (String info : collectResultVo.getContent()) {
                            contentLength += info.length();
                            content += "<p>" + info + "</p>";
                        }
                    }
                } catch (Exception e) {
                    log.error("获取热文选题信息失败", e);
                }
                selectTopic.setCharCount(contentLength);
                selectTopic.setContent(content);
                selectTopic.setIsDownload(true);
                selectTopic.setTitle(collectResultVo.getTitle());
            }
            selectTopicMapper.updateById(selectTopic);
            content = selectTopic.getContent();
            //读取当前文章内容
            //String content = "<div class=\"_3ygOc lg-fl \"><p></p><!--29--><!--30--><!--31--><!--32--><!--33--><!--34--><!--35--><!--36--><!--37--><p>用过高性价比手机的消费者，基本上都知道小米，因为小米可以说是高性价比手机的开创者了，正是在小米的带领下，市面上才出现了物美价廉的高性价比机型，但真正喜欢高性价比手机的消费者，所知道的高性价比手机品牌就不止小米了，例如目前realme这一品牌就和小米在高性价比手机领域竞争的非常激烈，它旗下机型性价比完全不输小米。</p><p></p></div><div class=\"_213jB lg-fl \"><!--38--><!--39--><div class=\"JmmC0\"><img src=\"https://pics7.baidu.com/feed/caef76094b36acafb9925b9eb94faf1c00e99c81.jpeg@f_auto?token=9b1e4dda71876f6e16250d16c3fbdd33\" width=\"640\" class=\"_1mscr\"><!--42--></div><!--40--><!--43--><!--44--><!--45--><!--46--><!--47--><!--48--></div><div class=\"_3ygOc lg-fl \"><p></p><!--49--><!--50--><!--51--><!--52--><!--53--><!--54--><!--55--><!--56--><!--57--><p>realme这一品牌背后有OPPO的扶持，主要目的就是想要抢占小米的份额，尤其是小米极致性价比子品牌redmi的份额，可能大家也发现了，两个品牌的命名也都有点相像，至于realme这一品牌旗下机型同样做到了极致性价比，而且这一品牌的存在对于消费者而言也是一件好事，因为它旗下机型可以覆盖到小米未注意的领域，例如realme 10 Pro+。</p><p></p></div><div class=\"_213jB lg-fl \"><!--58--><!--59--><div class=\"JmmC0\"><img src=\"https://pics0.baidu.com/feed/7c1ed21b0ef41bd57c64c0ae944ca3c738db3dd3.jpeg@f_auto?token=33d90ac08c8df1111e0780276f752671\" width=\"640\" class=\"_1mscr\"><!--62--></div><!--60--><!--63--><!--64--><!--65--><!--66--><!--67--><!--68--></div><div class=\"_3ygOc lg-fl \"><p></p><!--69--><!--70--><!--71--><!--72--><!--73--><!--74--><!--75--><!--76--><!--77--><p>realme 10 Pro+这款手机十分特殊，它不仅性价比高，而且还是难得的低价曲面屏手机，虽然小米子品牌redmi旗下机型性价比很高，但小米公司为了避免子品牌对主品牌造成冲击，所以强制redmi手机均只能启用直屏设计，这对于预算不多但喜欢曲面屏手机的消费者而言就不太好友了，但好在还有realme 10 Pro+，它就完全符合这类消费者的需求，因此卖得很好。</p><p></p></div><div class=\"_213jB lg-fl \"><!--78--><!--79--><div class=\"JmmC0\"><img src=\"https://pics3.baidu.com/feed/34fae6cd7b899e5174c98cdb8731fb3fca950dd8.jpeg@f_auto?token=2cf7358aba258ce2df992bfcfe312651\" width=\"640\" class=\"_1mscr\"><!--82--></div><!--80--><!--83--><!--84--><!--85--><!--86--><!--87--><!--88--></div><div class=\"_3ygOc lg-fl \"><p></p><!--89--><!--90--><!--91--><!--92--><!--93--><!--94--><!--95--><!--96--><!--97--><p>曲面屏之前一直都是高端旗舰的标配，但这款定价1699元起的手机却也配备了曲面屏，并且所配备的曲面屏档次还不低，120Hz高刷新率和10.7亿色原彩显示应有尽有，甚至这还是一块顶级护眼屏，支持了2160Hz高频PWM调光，并通过了无频闪低蓝光莱茵双认证，而且这款手机还不止屏幕好，它的处理器、电池以及摄像头用料都很实在。</p><p></p></div><div class=\"_213jB lg-fl \"><!--98--><!--99--><div class=\"JmmC0\"><img src=\"https://pics2.baidu.com/feed/cc11728b4710b91224513041046bde0f93452224.jpeg@f_auto?token=3e3c057d29b8458021940f56a0fb4231\" width=\"640\" class=\"_1mscr\"><!--102--></div><!--100--><!--103--><!--104--><!--105--><!--106--><!--107--><!--108--></div><div class=\"_3ygOc lg-fl \"><p></p><!--109--><!--110--><!--111--><!--112--><!--113--><!--114--><!--115--><!--116--><!--117--><p>作为一款1699元的低档机型，它搭载的是天玑1080这颗优质中低档处理器，即使是玩游戏也不用担心性能不够，至于它的电池容量则高达5000mAh，并且搭配上67W闪充，充满电的时间也很快，而它的后置摄像头启用的是一亿像素三摄，拍照表现也比同价位手机更为出色，包括X轴线性马达、立体声双扬声器和屏幕指纹它也全部都有。</p><p></p></div><div class=\"_213jB lg-fl \"><!--118--><!--119--><div class=\"JmmC0\"><img src=\"https://pics6.baidu.com/feed/a044ad345982b2b7864b3d64f73be9e374099bd8.jpeg@f_auto?token=0e842b8c11388937a5cf34a88b16462e\" width=\"640\" class=\"_1mscr\"><!--122--></div><!--120--><!--123--><!--124--><!--125--><!--126--><!--127--><!--128--></div><div class=\"_3ygOc lg-fl \"><p><span class=\"bjh-p\">这样一款手机放到现在来看也依然竞争力十足，但之所以说realme是小米的强敌，是因为它仍然舍得将高性价比手机降价来追求更高的销量，如今在realme官方品牌专卖店中，这款手机的标配版价格已经是被限时下调到了1349元沦为千元机了，千元机就能用上高端曲面屏和一亿像素三摄，放在以前真的是想都不敢想，由此可见这一品牌机型性价比究竟有多高。<span class=\"bjh-a\" data-bjh-src=\"https://m.baidu.com/s?word=%23%E6%95%B0%E7%A0%81%E5%AE%B6%E5%B1%85%E5%A5%BD%E7%89%A9%E5%A4%A7%E8%B5%8F%23&amp;topic_id=168267941016967307&amp;sa=edit&amp;sfrom=1023524a&amp;append=1&amp;newwindow=0&amp;upqrade=1\" data-bjh-type=\"topic\" data-bjh-id=\"168267941016967307\" data-bjh-cover=\"https://b0.bdstatic.com/topic/server/1/31fb94bc18e5.jpg\">#数码家居好物大赏#</span></span></p><!--129--><!--130--><!--131--><!--132--><!--133--><!--134--><!--135--><!--136--><!--137--></div><!--28--><!--138--><!--139--><div class=\"_213jB\"><div class=\"_1LjGN\"><span>举报/反馈</span></div></div>";
            String title = selectTopic.getTitle();
            WriteSelectedTopic writeSelectedTopic = new WriteSelectedTopic();
            writeSelectedTopic.setTitle(title);
            writeSelectedTopic.setContent(content);
            writeSelectedTopic.setIsComplete(false);
            writeSelectedTopic.setTopicId(id);
            writeSelectedTopic.setProjectId(projectId);
            writeSelectedTopic.setCreateUser(userId);
            writeSelectTopicMapper.insert(writeSelectedTopic);
            //生成到写作选题
            return new ResultEntity<>(true);
        } catch (RZException ex) {
            return new ResultEntity<>(0, ex.getMessage());
        } catch (Exception e) {
            return new ResultEntity<>(0, "系统异常");
        }


    }

    @Override
    public void uploadFile(MultipartFile file,Integer userId) {
        try {
            List<SelectTopicUploadVo> selectTopicUploadVos = EasyExcelUtil.readFromExcel(file.getInputStream(),
                    SelectTopicUploadVo.class, null, 0, 1);
            if (selectTopicUploadVos == null)
                return;
            //  List<SelectTopic> selectTopicList = new ArrayList<>();
            for (SelectTopicUploadVo item : selectTopicUploadVos) {
                if (!StringUtils.hasLength(item.getTitle())) {
                    continue;
                }
                if (!StringUtils.hasLength(item.getUrl())) {
                    continue;
                }
                SelectTopic newItem = new SelectTopic();
                newItem.setCreateUser(userId);
                BeanUtils.copyProperties(item, newItem);
                //  selectTopicList.add(newItem);
                selectTopicMapper.insert(newItem);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setRequestPage(SelectTopicGetListRequest request) {
        if (request.getPageIndex() == null || request.getPageIndex() < 1)
            request.setPageIndex(1);
        if (request.getPageSize() == null || request.getPageSize() < 1)
            request.setPageSize(20);
        request.setStartIndex((request.getPageIndex() - 1) * request.getPageSize());
    }
}
