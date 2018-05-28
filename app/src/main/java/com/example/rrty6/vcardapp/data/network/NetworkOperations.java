package com.example.rrty6.vcardapp.data.network;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.data.managers.DataManager;
import com.example.rrty6.vcardapp.data.network.model.req.CreateCardReq;
import com.example.rrty6.vcardapp.data.network.model.req.CreateGroupReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateCardReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateCardsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateContactsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateGroupReq;
import com.example.rrty6.vcardapp.data.network.model.req.UpdateGroupsReq;
import com.example.rrty6.vcardapp.data.network.model.req.UploadLogoReq;
import com.example.rrty6.vcardapp.data.network.model.req.UploadLogoReq.*;
import com.example.rrty6.vcardapp.data.network.model.req.UserRegisterReq;
import com.example.rrty6.vcardapp.data.network.model.res.CreateGroupRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetGroupRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetUserRes;
import com.example.rrty6.vcardapp.data.network.model.req.UserLoginReq;
import com.example.rrty6.vcardapp.data.network.model.res.CreateCardRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetCardRes;
import com.example.rrty6.vcardapp.data.network.model.res.GetFileRes;
import com.example.rrty6.vcardapp.data.network.model.res.LoginRes;
import com.example.rrty6.vcardapp.data.network.model.SimpleUnd;
import com.example.rrty6.vcardapp.data.network.model.res.TokenRes;
import com.example.rrty6.vcardapp.data.network.model.res.UploadLogoRes;
import com.example.rrty6.vcardapp.data.storage.CardCompare;
import com.example.rrty6.vcardapp.data.storage.GroupCompare;
import com.example.rrty6.vcardapp.data.storage.model.Base;
import com.example.rrty6.vcardapp.data.storage.model.Card;
import com.example.rrty6.vcardapp.data.storage.model.Email;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.data.storage.model.Logo;
import com.example.rrty6.vcardapp.data.storage.model.Phone;
import com.example.rrty6.vcardapp.data.storage.model.SocialLink;
import com.example.rrty6.vcardapp.data.storage.operation.DatabaseOperation;
import com.example.rrty6.vcardapp.utils.App;
import com.example.rrty6.vcardapp.utils.Const.ListRole;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rrty6.vcardapp.utils.Const.*;

public class NetworkOperations {
    private static final String TAG = "NetworkOperations";
    private static DataManager mDataManager = DataManager.getInstance();

    public static void register(final String email, final String password) throws Exception {
        Call<ResponseBody> call = mDataManager.register(new UserRegisterReq(email, password));
        final Exception[] exception = {null};
        if (call != null) {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        signIn(email, password);
                    } catch (Exception e) {
                        exception[0] = new Exception(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
            if (exception[0] != null) {
                throw exception[0];
            }
        } else {
            throw new Exception("User is already autorized");
        }
    }

    public static void signIn(String email, String password) throws Exception {
        Call<LoginRes> call = mDataManager.logginUser(new UserLoginReq(email, password));
        //call will be null for authorized user
        if (call != null) {
            call.enqueue(new Callback<LoginRes>() {
                @Override
                public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                    if (response.code() == 200 && response.body() != null) {
                        //Gets session
                        mDataManager.getPreferenceManager().saveCookie(response.headers().get("Set-Cookie"));
                        //Gets token
                        mDataManager.getPreferenceManager().saveToken(response.body().getToken());
                        //Gets UserRes Id
                        mDataManager.getPreferenceManager().saveUserID(response.body().getUser().getUid());
                        try {
                            MainOperations.downloadUserData();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Null Call<LoginRes> response");
                        //TODO: play around null SignIn response
                    }
                }

                @Override
                public void onFailure(Call<LoginRes> call, Throwable t) {
                    //TODO: обработать ошибки retrofit2
                }
            });
        } else {
            throw new Exception("User is already autorized");
        }
    }

    public static void logOut() throws Exception {
        Call<ResponseBody> call = mDataManager.logOut();
        //call will be null for unauthorized user
        if (call != null) {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        mDataManager.getPreferenceManager().logoutUser();
                        try {
                            DatabaseOperation.clearDb();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //TODO: play around null GetAllCards response
                    //TODO: add error handler
                }
            });
        } else {
            throw new Exception("User hasn't been autorized");
        }
    }

    public static void getToken() {
        Call<TokenRes> call = mDataManager.getToken();
        call.enqueue(new Callback<TokenRes>() {
            @Override
            public void onResponse(Call<TokenRes> call, Response<TokenRes> response) {
                if (response.code() == 200 && response.body() != null) {
                    Toast.makeText(App.getContext(), response.body().getToken(), Toast.LENGTH_SHORT).show();
                    System.out.println(response.body().getToken());
                } else {
                    System.out.println("Null response");
                }
            }

            @Override
            public void onFailure(Call<TokenRes> call, Throwable t) {
                //TODO: add error handler
            }
        });
    }

    public static Logo createLogo(final Logo logo, Filepath filepath) {
        if (logo != null) {
            Call<UploadLogoRes> call = mDataManager.sendLogo(new UploadLogoReq(logo.getFilename(), filepath, logo.getLogo()));
            if (call != null) {
                Response<UploadLogoRes> response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    if (response.code() == 200 && response.body() != null) {
                        return new Logo(response.body().getFid(), logo.getFilename(), logo.getLogo());
                    }
                } else {
                    //TODO: add error handler
                }
            } else {
                System.out.println("Null call");
            }
        }
        return null;
    }

    public static Logo getLogo(final String fid) {
        final Call<GetFileRes> call = mDataManager.getLogo(fid);
        final Logo[] logo = {null};
        Thread thread = new Thread(new Runnable() {
            public void run() {
                if (call != null) {
                    Response<GetFileRes> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response != null) {
                        if (response.code() == 200 && response.body() != null) {
                            logo[0] = new Logo(fid, response.body().getFilename(), response.body().getFile());
                        }
                    } else {
                        //TODO: add error handler
                    }
                } else {
                    System.out.println("Null call");
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return logo[0];
    }

    public static List<Card> downloadMyCards() {
        final Call<GetUserRes> call = mDataManager.getUser();
        final List<Card>[] cards = new List[]{new ArrayList<>()};

        if (call != null) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    Response<GetUserRes> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response != null) {
                        if (response.code() == 200 && response.body() != null) {
                            List<String> existedCards = new ArrayList<>();

                            try {
                                List list = (List) ((Map) response.body().getFieldMyVcards())
                                        .get(((Map) response.body().getFieldMyVcards())
                                                .keySet().iterator().next());

                                for (int i = 0; i < list.size(); i++) {
                                    Map o = (Map) list.get(i);
                                    Set set = o.keySet();

                                    existedCards.add(o.get(set.iterator().next()).toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            for (String s : existedCards) {
                                cards[0].add(downloadCard(s, true));
                            }
                        }
                    } else {
                        //TODO: add error handler
                    }
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return cards[0];
    }

    public static Card downloadCard(final String cardId, final boolean isMy) {
        final Call<GetCardRes> call = mDataManager.getCard(cardId);
        final Card[] card = {null};


        //call will be null for unauthorized user
        if (call != null) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    Response<GetCardRes> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response != null) {
                        if (response.code() == 200 && response.body() != null) {
                            card[0] = new Card();

                            card[0].setRemoteId(cardId);

                            card[0].setMy(isMy);

                            card[0].setTitle(response.body().getTitle());

                            card[0].setAddress(response.body().getAddress());

                            card[0].setCompany(response.body().getCompany());

                            try {
                                String s = null;
                                List list = (List) ((Map) response.body().getFieldLogotype())
                                        .get(((Map) response.body().getFieldLogotype())
                                                .keySet().iterator().next());
                                for (int i = 0; i < list.size(); i++) {
                                    Map o = (Map) list.get(i);
                                    Set set = o.keySet();
                                    s = o.get(set.iterator().next()).toString();
                                }
                                Logo logo = getLogo(s);
                                DataManager.getInstance().addLogo(logo);
                                card[0].setLogo(logo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            try {
//                                Logo logo = getLogo(response.body().getFieldLogotype().getUnd().get(0).getFid());
//                                DataManager.getInstance().addLogo(logo);
//                                card[0].setLogo(logo);
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }

                            card[0].setMidlename(response.body().getMiddleName());

                            card[0].setName(response.body().getName());

                            card[0].setPosition(response.body().getPosition());

                            card[0].setSurname(response.body().getSurname());

                            card[0].setSite(response.body().getWebSite());

                            List<SocialLink> links = null;
                            try {
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Object o = gson.fromJson(response.body().getSocialLinks(), Object.class);
                                Map<String, String> map = (Map) o;
                                Set<String> set = map.keySet();
                                links = new ArrayList<>();
                                for (String type : set) {
                                    if (map.get(type) != null && !map.get(type).isEmpty()) {
                                        SocialLink link = new SocialLink(LinkType.getType(type), map.get(type));
                                        links.add(link);
                                    }
                                }
                            } catch (Exception e) {
//                                e.printStackTrace();
                            }
                            card[0].setSocialLinks(links);

                            card[0].setBase(new Base(response.body().getBase64Vcard()));

                            card[0].addPhone(new Phone(response.body().getPhone()));

                            card[0].addEmail(new Email(response.body().getEmail()));
                        } else {
                            System.out.println("Null Call<GetCardRes> response");
                            //TODO: play around null GetCard response
                        }
                    } else {
                        //TODO: add error handler
                    }
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return card[0];
    }

    public static List<Card> downloadMyContacts() {
        final Call<GetUserRes> call = mDataManager.getUser();
        final List<Card>[] cards = new List[]{new ArrayList()};

        if (call != null) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    Response<GetUserRes> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response != null) {
                        if (response.code() == 200 && response.body() != null) {
                            List<String> existedContacts = new ArrayList<>();

                            try {
                                List list = (List) ((Map) response.body().getFieldMyContacts())
                                        .get(((Map) response.body().getFieldMyContacts())
                                                .keySet().iterator().next());

                                for (int i = 0; i < list.size(); i++) {
                                    Map o = (Map) list.get(i);
                                    Set set = o.keySet();

                                    existedContacts.add(o.get(set.iterator().next()).toString());
                                }
                            } catch (Exception e) {
//                                e.printStackTrace();
                            }

                            for (String s : existedContacts) {
                                cards[0].add(downloadCard(s, false));
                            }
                        }
                    } else {
                        //TODO: add error handler
                    }
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return cards[0];
    }

    public static List<Group> getMyGroups() {
        final Call<GetUserRes> call = mDataManager.getUser();
        final List<Group>[] groups = new List[]{new ArrayList<>()};

        if (call != null) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    Response<GetUserRes> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response != null) {
                        if (response.code() == 200 && response.body() != null) {
                            List<String> existedGroups = new ArrayList<>();

                            try {
                                List list = (List) ((Map) response.body().getFieldGroups())
                                        .get(((Map) response.body().getFieldGroups())
                                                .keySet().iterator().next());

                                for (int i = 0; i < list.size(); i++) {
                                    Map o = (Map) list.get(i);
                                    Set set = o.keySet();

                                    existedGroups.add(o.get(set.iterator().next()).toString());
                                }
                            } catch (Exception e) {
//                                e.printStackTrace();
                            }

                            for (String s : existedGroups) {
                                groups[0].add(getGroup(s));
                            }
                        }
                    } else {
                        //TODO: add error handler
                    }
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return groups[0];
    }

    public static Group getGroup(String id) {
        final Call<GetGroupRes> call = mDataManager.getGroup(id);
        final Group[] group = {null};
        if (call != null) {
            Thread thread = new Thread(new Runnable() {
                public void run() {

                    Response<GetGroupRes> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response != null) {
                        if (response.code() == 200 && response.body() != null) {
                            List list;
                            group[0] = new Group();
                            try {
                                group[0].setRemoteId(response.body().getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            group[0].setName(response.body().getGroupName());
                            group[0].setDescription(response.body().getDescription());

                            try {
                                list = (List) ((Map) response.body().getFieldLogotype())
                                        .get(((Map) response.body().getFieldLogotype()).keySet().iterator().next());
                                Map o = (Map) list.get(0);
                                Set set = o.keySet();

                                Logo logo = getLogo(o.get(set.iterator().next()).toString());
                                DataManager.getInstance().addLogo(logo);
                                group[0].setLogo(logo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //TODO: add error handler
                    }
                }

            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Null call");
        }
        return group[0];
    }

    public static List<String> getGroupContacts(final Group group) {
        final Call<GetGroupRes> call = mDataManager.getGroup(group.getRemoteId());
        final List<String> strings = new ArrayList<>();
        if (call != null) {
            Thread thread = new Thread(new Runnable() {
                public void run() {

                    Response<GetGroupRes> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response != null) {
                        if (response.code() == 200 && response.body() != null) {
                            List list;
                            try {
                                list = (List) ((Map) response.body().getFieldMyContacts())
                                        .get(((Map) response.body().getFieldMyContacts()).keySet().iterator().next());

                                for (int i = 0; i < list.size(); i++) {
                                    Map o = (Map) list.get(i);
                                    Set set = o.keySet();
                                    strings.add(o.get(set.iterator().next()).toString());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        System.out.println("out");//TODO: add error handler
                    }
                }

            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Null call");
        }
        return strings;
    }

    public static void createCard(final Card card) {
        if (!card.isMy()) {
            return;
        }
        CreateCardReq createCardReq = new CreateCardReq(card);
        Call<CreateCardRes> call = mDataManager.sendCard(createCardReq);
        if (call != null) {
            call.enqueue(new Callback<CreateCardRes>() {
                @Override
                public void onResponse(Call<CreateCardRes> call, Response<CreateCardRes> response) {
                    if (response.code() == 200 && response.body() != null) {
                        addUserGoods(ListRole.card, new SimpleUnd(response.body().getId()));
                        card.setRemoteId(response.body().getId());
                        try {
                            DataManager.getInstance().updateCard(card);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CreateCardRes> call, Throwable t) {

                }
            });
        } else {
            System.out.println("Null call");
        }
    }

    //TODO: logo update finish
    public static void updateCard(Card card, CardCompare compare) {
        if (card.isMy()) {
            UpdateCardReq updateCardReq = new UpdateCardReq(card, compare);
            Call<ResponseBody> call = mDataManager.updateCard(updateCardReq, card.getRemoteId());
            if (call != null) {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            } else {
                System.out.println("Null call");
            }
        }
    }

    public static void deleteCard(final Card card) {
        Log.i(TAG,"deleteCard start");
        Call<GetUserRes> call = mDataManager.getUser();

        if (call != null) {
            call.enqueue(new Callback<GetUserRes>() {
                @Override
                public void onResponse(Call<GetUserRes> call, Response<GetUserRes> response) {

                    if (response.code() == 200 && response.body() != null) {
                        List<SimpleUnd> existedCards = new ArrayList<>();

                        try {
                            List list = (List) ((Map) response.body().getFieldMyVcards())
                                    .get(((Map) response.body().getFieldMyVcards())
                                            .keySet().iterator().next());

                            for (int i = 0; i < list.size(); i++) {
                                Map o = (Map) list.get(i);
                                Set set = o.keySet();
                                String s = o.get(set.iterator().next()).toString();

                                existedCards.add(new SimpleUnd(s));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < existedCards.size(); i++) {
                            if (existedCards.get(i).getTarget_id().equals(card.getRemoteId())) {
                                existedCards.remove(i);
                                continue;
                            }
//                            String s = existedCards.get(i).getTarget_id();
//                            existedCards.get(i).setTarget_id(s + "(" + s + ")");
                        }


//                        for (SimpleUnd u : existedCards) {
//                            String s = u.getTarget_id();
//                            u.setTarget_id(s + "(" + s + ")");
//                        }

                        updateUser(new UpdateCardsReq(existedCards));
                    }
                }

                @Override
                public void onFailure(Call<GetUserRes> call, Throwable t) {

                }
            });
        }
    }

    public static void addContact(Card card) {
        if (card.isMy()) {
            return;
        }
        addUserGoods(ListRole.contact, new SimpleUnd(card.getRemoteId()));

    }

    public static void deleteContact(final Card card) {
        Call<GetUserRes> call = mDataManager.getUser();

        if (call != null) {
            call.enqueue(new Callback<GetUserRes>() {
                @Override
                public void onResponse(Call<GetUserRes> call, Response<GetUserRes> response) {

                    if (response.code() == 200 && response.body() != null) {
                        List<SimpleUnd> existedContacts = new ArrayList<>();

                        try {
                            List list = (List) ((Map) response.body().getFieldMyContacts())
                                    .get(((Map) response.body().getFieldMyContacts())
                                            .keySet().iterator().next());

                            for (int i = 0; i < list.size(); i++) {
                                Map o = (Map) list.get(i);
                                Set set = o.keySet();
                                String s = o.get(set.iterator().next()).toString();

                                existedContacts.add(new SimpleUnd(s));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < existedContacts.size(); i++) {
                            if (existedContacts.get(i).getTarget_id().equals(card.getRemoteId())) {
                                existedContacts.remove(i);
                                continue;
                            }
//                            String s = existedContacts.get(i).getTarget_id();
//                            existedContacts.get(i).setTarget_id(s + "(" + s + ")");
                        }


//                        for (SimpleUnd u : existedContacts) {
//                            String s = u.getTarget_id();
//                            u.setTarget_id(s + "(" + s + ")");
//                        }

                        updateUser(new UpdateContactsReq(existedContacts));
                    }
                }

                @Override
                public void onFailure(Call<GetUserRes> call, Throwable t) {

                }
            });
        }
    }

    public static void createGroup(@NonNull final Group group, List<Card> contacts) {
        if (group != null) {
            if (group.getName() != null && !group.getName().isEmpty()) {
                CreateGroupReq createGroupReq = new CreateGroupReq(group.getName());

                if (group.getDescription() != null && !group.getDescription().isEmpty()) {
                    createGroupReq.setDescription(group.getDescription());
                }

                if (group.getLogo()!=null) {
                    createGroupReq.fieldLogotype.setLogoUnd(group.getLogo().getFid());
                }

                if (contacts != null && contacts.size() != 0) {
                    for (Card card : contacts) {
                        createGroupReq.fieldMyContacts.addUnd(new SimpleUnd(card.getRemoteId()));
                    }
                }

                Call<CreateGroupRes> call = mDataManager.sendGroup(createGroupReq);

                call.enqueue(new Callback<CreateGroupRes>() {
                    @Override
                    public void onResponse(Call<CreateGroupRes> call, Response<CreateGroupRes> response) {
                        addUserGoods(ListRole.group, new SimpleUnd(response.body().getId()));
                        group.setRemoteId(response.body().getId());
                        try {
                            DataManager.getInstance().updateGroup(group);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateGroupRes> call, Throwable t) {

                    }
                });
            } else {
                throw new IllegalArgumentException("Group name can't be null or empty");
            }
        } else {
            throw new IllegalArgumentException("Null method argument");
        }
    }

    public static void updateGroup(Group group, GroupCompare compare) {
        if (group.getRemoteId() != null && !group.getRemoteId().isEmpty()) {
            UpdateGroupReq updateGroupReq = new UpdateGroupReq(group, compare);
            Call<ResponseBody> call = mDataManager.updateGroup(updateGroupReq);
            if (call != null) {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            } else {
                System.out.println("Null call");
            }
        }
    }

    public static void updateGroupContacts(Group group, List<Card> contacts) {
        UpdateGroupReq updateGroupReq = new UpdateGroupReq(group, contacts);
        Call<ResponseBody> call = mDataManager.updateGroup(updateGroupReq);
        if (call != null) {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            System.out.println("Null call");
        }
    }

    public static void deleteGroup(final Group group) {
        Call<GetUserRes> call = mDataManager.getUser();

        if (call != null) {
            call.enqueue(new Callback<GetUserRes>() {
                @Override
                public void onResponse(Call<GetUserRes> call, Response<GetUserRes> response) {

                    if (response.code() == 200 && response.body() != null) {
                        List<SimpleUnd> existedGroups = new ArrayList<>();

                        try {
                            List list = (List) ((Map) response.body().getFieldGroups())
                                    .get(((Map) response.body().getFieldGroups())
                                            .keySet().iterator().next());

                            for (int i = 0; i < list.size(); i++) {
                                Map o = (Map) list.get(i);
                                Set set = o.keySet();
                                String s = o.get(set.iterator().next()).toString();

                                existedGroups.add(new SimpleUnd(s));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < existedGroups.size(); i++) {
                            if (existedGroups.get(i).getTarget_id().equals(group.getRemoteId())) {
                                existedGroups.remove(i);
                                continue;
                            }
//                            String s = existedGroups.get(i).getTarget_id();
//                            existedGroups.get(i).setTarget_id(s + "(" + s + ")");
                        }


//                        for (SimpleUnd u : existedGroups) {
//                            String s = u.getTarget_id();
//                            u.setTarget_id(s + "(" + s + ")");
//                        }

                        updateUser(new UpdateGroupsReq(existedGroups));
                    }
                }

                @Override
                public void onFailure(Call<GetUserRes> call, Throwable t) {

                }
            });
        }
    }

    private static void addUserGoods(final ListRole role, final SimpleUnd und) {
        Log.i(TAG,"addUserGoods");
        Call<GetUserRes> call = mDataManager.getUser();

        if (call != null) {
            call.enqueue(new Callback<GetUserRes>() {
                @Override
                public void onResponse(Call<GetUserRes> call, Response<GetUserRes> response) {

                    if (response.code() == 200 && response.body() != null) {
                        switch (role) {
                            case card:
                                Log.i(TAG,"addUserGoods role: card");
                                List<SimpleUnd> existedCards = new ArrayList<>();

                                try {
                                    List list = (List) ((Map) response.body().getFieldMyVcards())
                                            .get(((Map) response.body().getFieldMyVcards())
                                                    .keySet().iterator().next());

                                    for (int i = 0; i < list.size(); i++) {
                                        Map o = (Map) list.get(i);
                                        Set set = o.keySet();
                                        String s = o.get(set.iterator().next()).toString();

                                        existedCards.add(new SimpleUnd(s));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                existedCards.add(und);
                                for (SimpleUnd u : existedCards) {
//                                    String s = u.getTarget_id();
//                                    u.setTarget_id(s + "(" + s + ")");
                                    u.setTarget_id(u.getTarget_id());
                                }

                                updateUser(new UpdateCardsReq(existedCards));
                                break;
                            case contact:
                                List<SimpleUnd> existedContacts = new ArrayList<>();

                                try {
                                    List list = (List) ((Map) response.body().getFieldMyContacts())
                                            .get(((Map) response.body().getFieldMyContacts())
                                                    .keySet().iterator().next());

                                    for (int i = 0; i < list.size(); i++) {
                                        Map o = (Map) list.get(i);
                                        Set set = o.keySet();
                                        String s = o.get(set.iterator().next()).toString();

                                        existedContacts.add(new SimpleUnd(s));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                existedContacts.add(und);
                                for (SimpleUnd u : existedContacts) {
//                                    String s = u.getTarget_id();
//                                    u.setTarget_id(s + "(" + s + ")");
                                    u.setTarget_id(u.getTarget_id());
                                }

                                updateUser(new UpdateContactsReq(existedContacts));
                                break;
                            case group:
                                List<SimpleUnd> existedGroups = new ArrayList<>();

                                try {
                                    List list = (List) ((Map) response.body().getFieldGroups())
                                            .get(((Map) response.body().getFieldGroups())
                                                    .keySet().iterator().next());

                                    for (int i = 0; i < list.size(); i++) {
                                        Map o = (Map) list.get(i);
                                        Set set = o.keySet();
                                        String s = o.get(set.iterator().next()).toString();

                                        existedGroups.add(new SimpleUnd(s));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                existedGroups.add(und);
                                for (SimpleUnd u : existedGroups) {
//                                    String s = u.getTarget_id();
//                                    u.setTarget_id(s + "(" + s + ")");
                                    u.setTarget_id(u.getTarget_id());
                                }

                                updateUser(new UpdateGroupsReq(existedGroups));
                                break;
                        }


                    }
                }

                @Override
                public void onFailure(Call<GetUserRes> call, Throwable t) {

                }
            });
        }
    }

    private static void updateUser(UpdateCardsReq collection) {
        Call<ResponseBody> call = mDataManager.updateUser(collection);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private static void updateUser(UpdateContactsReq collection) {
        Call<ResponseBody> call = mDataManager.updateUser(collection);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private static void updateUser(UpdateGroupsReq collection) {
        Call<ResponseBody> call = mDataManager.updateUser(collection);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
