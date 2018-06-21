package com.bindup.vcard.vcardapp.data.network;

import com.bindup.vcard.vcardapp.data.network.model.req.CreateCardReq;
import com.bindup.vcard.vcardapp.data.network.model.req.CreateGroupReq;
import com.bindup.vcard.vcardapp.data.network.model.req.UpdateCardReq;
import com.bindup.vcard.vcardapp.data.network.model.req.UpdateCardsReq;
import com.bindup.vcard.vcardapp.data.network.model.req.UpdateContactsReq;
import com.bindup.vcard.vcardapp.data.network.model.req.UpdateGroupReq;
import com.bindup.vcard.vcardapp.data.network.model.req.UpdateGroupsReq;
import com.bindup.vcard.vcardapp.data.network.model.req.UploadLogoReq;
import com.bindup.vcard.vcardapp.data.network.model.req.UserLoginReq;
import com.bindup.vcard.vcardapp.data.network.model.req.UserRegisterReq;
import com.bindup.vcard.vcardapp.data.network.model.res.CreateCardRes;
import com.bindup.vcard.vcardapp.data.network.model.res.CreateGroupRes;
import com.bindup.vcard.vcardapp.data.network.model.res.GetAllCardRes;
import com.bindup.vcard.vcardapp.data.network.model.res.GetCardRes;
import com.bindup.vcard.vcardapp.data.network.model.res.GetFileRes;
import com.bindup.vcard.vcardapp.data.network.model.res.GetGroupRes;
import com.bindup.vcard.vcardapp.data.network.model.res.GetUserRes;
import com.bindup.vcard.vcardapp.data.network.model.res.HistoryRes;
import com.bindup.vcard.vcardapp.data.network.model.res.TokenRes;
import com.bindup.vcard.vcardapp.data.network.model.res.UploadLogoRes;
import com.bindup.vcard.vcardapp.data.network.model.res.LoginRes;
import com.bindup.vcard.vcardapp.data.storage.model.History;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

//Interface for Retrofit
public interface RestService {

    @Headers("Content-Type: application/json")
    @POST("user/register")
    Call<ResponseBody> register(@Body UserRegisterReq req);

    @Headers("Content-Type: application/json")
    @POST("user/login")
    Call<LoginRes> loginUser(@Body UserLoginReq req);

    @Headers("Content-Type: application/json")
    @POST("user/token")
    Call<TokenRes> getToken(@Header("Cookie") String cookie);

    @Headers("Content-Type: application/json")
    @POST("user/logout")
    Call<ResponseBody> logOut(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token);

    @Headers("Content-Type: application/json")
    @POST("entity_vcard")
    Call<CreateCardRes> createCard(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Body CreateCardReq req);

    @Headers("Content-Type: application/json")
    @PUT("entity_vcard/{nid}")
    Call<ResponseBody> updateCard(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Path("nid") String cardId,
                                  @Body UpdateCardReq req);

    Call<ResponseBody> addHistory();//TODO: Complete method

    Call<HistoryRes> downloadHistory();//TODO: Complete method

    @Headers("Content-Type: application/json")
    @DELETE("entity_vcard/{nid}")
    Call<ResponseBody> deleteCard(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Path("nid") String cardId);

    @Headers("Content-Type: application/json")
    @POST("entity_group")
    Call<CreateGroupRes> createGroup(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Body CreateGroupReq req);

    @Headers("Content-Type: application/json")
    @POST("file")
    Call<UploadLogoRes> uploadLogo(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Body UploadLogoReq req);

    @Headers("Content-Type: application/json")
    @GET("file/{fid}")
    Call<GetFileRes> downloadLogo(@Header("Cookie") String cookie, @Path("fid") String fid);

    @Headers("Content-Type: application/json")
    @GET("entity_group/{id}")
    Call<GetGroupRes> downloadGroup(@Header("Cookie") String cookie, @Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("entity_vcard/{nid}")
    Call<GetCardRes> getCard(@Header("Cookie") String cookie, @Path("nid") String cardNumber);

    @Headers("Content-Type: application/json")
    @GET("node")
    Call<List<GetAllCardRes>> getAllCards(@Header("Cookie") String cookie, @Query("parameters[uid]") String userID, @Query("fields") String s);

    @Headers("Content-Type: application/json")
    @GET("user/{uid}")
    Call<GetUserRes> getUser(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Path("uid") String userId);

    @Headers("Content-Type: application/json")
    @PUT("entity_user/{uid}")
    Call<ResponseBody> updateUser(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Path("uid") String userId,
                                  @Body UpdateCardsReq req);

    @Headers("Content-Type: application/json")
    @PUT("entity_user/{uid}")
    Call<ResponseBody> updateUser(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Path("uid") String userId,
                                  @Body UpdateContactsReq req);

    @Headers("Content-Type: application/json")
    @PUT("entity_user/{uid}")
    Call<ResponseBody> updateUser(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Path("uid") String userId,
                                  @Body UpdateGroupsReq req);

    @Headers("Content-Type: application/json")
    @PUT("entity_group/{fid}")
    Call<ResponseBody> updateGroup(@Header("Cookie") String cookie, @Header("X-CSRF-TOKEN") String token, @Path("fid") String groupId,
                                   @Body UpdateGroupReq req);
}
