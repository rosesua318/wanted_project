package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.community.model.*;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/communities")
public class CommunityController {

    @Autowired
    private final CommunityProvider communityProvider;

    @Autowired
    private final CommunityService communityService;

    @Autowired
    private final JwtService jwtService;

    private final ImageUploader imageUploader;

    public CommunityController(CommunityProvider communityProvider, CommunityService communityService, JwtService jwtService, ImageUploader imageUploader) {
        this.communityProvider = communityProvider;
        this.communityService = communityService;
        this.jwtService = jwtService;
        this.imageUploader = imageUploader;
    }

    /**
     * 커뮤니티 그외 탭 조회 (비회원용) API
     * [GET] /communities
     * @return BaseResponse<GetOtherOpenRes>
     */
    @ResponseBody
    @GetMapping()
    public BaseResponse<GetOtherOpenRes> getOtherTabOpen(@RequestParam(name="ctIdx") int ctIdx) throws BaseException {
        try {
            GetOtherOpenRes getOtherRes = communityProvider.getOtherTabOpen(ctIdx);
            return new BaseResponse<>(getOtherRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 그외 탭 조회 (회원용) API
     * [GET] /communities/:userIdx
     * @return BaseResponse<GetOtherRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetOtherRes> getOtherTab(@PathVariable("userIdx") int userIdx, @RequestParam(name="ctIdx") int ctIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetOtherRes getOtherRes = communityProvider.getOtherTab(userIdx, ctIdx);
            return new BaseResponse<>(getOtherRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 전체 탭 조회 (비회원용) API
     * [GET] /communities/totals
     * @return BaseResponse<GetAllOpenRes>
     */
    @ResponseBody
    @GetMapping("/totals")
    public BaseResponse<GetAllOpenRes> getAllTabOpen() throws BaseException {
        try {
            GetAllOpenRes getAllRes = communityProvider.getAllTabOpen();
            return new BaseResponse<>(getAllRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 전체 탭 조회 (회원용) API
     * [GET] /communities/totals/:userIdx
     * @return BaseResponse<GetAllRes>
     */
    @ResponseBody
    @GetMapping("/totals/{userIdx}")
    public BaseResponse<GetAllRes> getAllTab(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetAllRes getAllRes = communityProvider.getAllTab(userIdx);
            return new BaseResponse<>(getAllRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 추천 탭 조회 (비회원용) API
     * [GET] /communities/recommends
     * @return BaseResponse<GetRecommOpenRes>
     */
    @ResponseBody
    @GetMapping("/recommends")
    public BaseResponse<GetRecommOpenRes> getRecommTabOpen() throws BaseException {
        try {
            GetRecommOpenRes getRecommRes = communityProvider.getRecommTabOpen();
            return new BaseResponse<>(getRecommRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 추천 탭 조회 (회원용) API
     * [GET] /communities/recommends/:userIdx
     * @return BaseResponse<GetRecommRes>
     */
    @ResponseBody
    @GetMapping("/recommends/{userIdx}")
    public BaseResponse<GetRecommRes> getRecommTab(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetRecommRes getRecommRes = communityProvider.getRecommTab(userIdx);
            return new BaseResponse<>(getRecommRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 상세 조회 (비회원용) API
     * [GET] /communities/postings/:postingIdx
     * @return BaseResponse<GetPostingOpenRes>
     */
    @ResponseBody
    @GetMapping("/postings/{postingIdx}")
    public BaseResponse<GetPostingOpenRes> getPostingOpen(@PathVariable("postingIdx") int postingIdx) throws BaseException {
        try {
            GetPostingOpenRes getPostingRes = communityProvider.getPostingOpen(postingIdx);
            return new BaseResponse<>(getPostingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 상세 조회 (회원용) API
     * [GET] /communities/postings/:postingIdx/:userIdx
     * @return BaseResponse<GetPostingRes>
     */
    @ResponseBody
    @GetMapping("/postings/{postingIdx}/{userIdx}")
    public BaseResponse<GetPostingRes> getPosting(@PathVariable("postingIdx") int postingIdx, @PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetPostingRes getPostingRes = communityProvider.getPosting(postingIdx, userIdx);
            return new BaseResponse<>(getPostingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 프로필 설정 조회 API
     * [GET] /communities/profiles/:userIdx
     * @return BaseResponse<GetProfileRes>
     */
    @ResponseBody
    @GetMapping("/profiles/{userIdx}")
    public BaseResponse<GetProfileRes> getProfile(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetProfileRes getProfileRes = communityProvider.getProfile(userIdx);
            return new BaseResponse<>(getProfileRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 프로필 설정 조회 API
     * [GET] /communities/profiles/:userIdx
     * @return BaseResponse<GetProfileRes>
     */
    @ResponseBody
    @PatchMapping("/profiles/{userIdx}")
    public BaseResponse<String> getProfile(@PathVariable("userIdx") int userIdx, @RequestBody PatchProfileReq patchProfileReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            communityService.setProfile(userIdx, patchProfileReq);
            return new BaseResponse<>("변경 되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 작성 API
     * [POST] /communities/:userIdx
     * @return BaseResponse<PostPostingRes>
     */
    @ResponseBody
    @PostMapping(value = "/{userIdx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<PostPostingRes> createPosting(@PathVariable("userIdx") int userIdx, @RequestParam(value = "image", required = false) MultipartFile multipartFile, @RequestParam("json") String json) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
            PostPostingReq postPostingReq = objectMapper.readValue(json, new TypeReference<PostPostingReq>() {
            });
            if(postPostingReq.getTags().size() < 1) {
                return new BaseResponse<>(POST_POSTING_NO_TAG);
            }
            if(postPostingReq.getTitle().equals("")) {
                return new BaseResponse<>(POST_POSTING_NO_TITLE);
            }
            if(postPostingReq.getContent().equals("")) {
                return new BaseResponse<>(POST_POSTING_NO_CONTENT);
            }
            System.out.println(multipartFile);
            String imageUrl = "";
            if(multipartFile != null) {
                if(!multipartFile.isEmpty()) {
                    imageUrl = imageUploader.upload(multipartFile, "static");
                }
            }

            PostPostingRes postPostingRes;
            if(imageUrl.equals("") || imageUrl.isEmpty()) {
                postPostingRes = communityService.createPosting(userIdx, postPostingReq);
            } else {
                postPostingRes = communityService.createPostingWithImage(userIdx, imageUrl, postPostingReq);
            }
            return new BaseResponse<>(postPostingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        } catch (IOException exception) {
            return new BaseResponse<>(FAIL_IMAGE_UPLOAD);
        }
    }
}
