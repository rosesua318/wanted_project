package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.PatchLikeReq;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.like.model.PostLikeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.PATCH_LIKES_NO_DATA;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private final LikeProvider likeProvider;

    @Autowired
    private final LikeService likeService;


    public LikeController(LikeProvider likeProvider, LikeService likeService) {
        this.likeProvider = likeProvider;
        this.likeService = likeService;
    }

    /**
     * 좋아요 조회 API
     * [GET] /likes/:userIdx
     * @return BaseResponse<List<GetLikeRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetLikeRes>> getLikes(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            List<GetLikeRes> getLikeRes = likeProvider.getLikes(userIdx);
            return new BaseResponse<>(getLikeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 좋아요 등록 API
     * [POST] /likes/:userIdx
     * @return BaseResponse<PostLikeRes>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostLikeRes> likes(@PathVariable("userIdx") int userIdx, @RequestBody PostLikeReq postLikeReq) throws BaseException {
        try {
            if(likeProvider.checkLikes(userIdx, postLikeReq.getEmploymentIdx()) != 0) {
                int likeIdx = likeService.modifyLikes(userIdx, postLikeReq.getEmploymentIdx());
                return new BaseResponse<>(new PostLikeRes(likeIdx));
            } else {
                int likeIdx = likeService.createLikes(userIdx, postLikeReq.getEmploymentIdx());
                return new BaseResponse<>(new PostLikeRes(likeIdx));
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 좋아요 삭제 API
     * [PATCH] /likes/:userIdx
     * @return BaseResponse<PatchLikeRes>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> deletelikes(@PathVariable("userIdx") int userIdx, @RequestBody PatchLikeReq patchLikeReq) throws BaseException {
        try {
            if(likeProvider.checkLikes(userIdx, patchLikeReq.getEmploymentIdx()) != 0) {
                likeService.deleteLikes(userIdx, patchLikeReq.getEmploymentIdx());
                return new BaseResponse<>("삭제되었습니다.");
            } else {
                return new BaseResponse<>(PATCH_LIKES_NO_DATA);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
