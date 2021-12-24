package com.example.news.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class News_brief_Bean {
    private int code;
    private String msg;
    private DataDTO data;

    public static News_brief_Bean objectFromData(String str) {

        return new Gson().fromJson(str, News_brief_Bean.class);
    }

    public static News_brief_Bean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), News_brief_Bean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<News_brief_Bean> arrayNews_brief_BeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<News_brief_Bean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<News_brief_Bean> arrayNews_brief_BeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<News_brief_Bean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private int count;
        private List<NewsDTO> news;

        public static DataDTO objectFromData(String str) {

            return new Gson().fromJson(str, DataDTO.class);
        }

        public static DataDTO objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), DataDTO.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<DataDTO> arrayDataDTOFromData(String str) {

            Type listType = new TypeToken<ArrayList<DataDTO>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<DataDTO> arrayDataDTOFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<DataDTO>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<NewsDTO> getNews() {
            return news;
        }

        public void setNews(List<NewsDTO> news) {
            this.news = news;
        }

        public static class NewsDTO {
            private int id;
            private List<String> news_pics_set;
            private String title;
            private String content;
            private String create_time;
            private int status;
            private int like_num;
            private int star_num;
            private int bb_num;
            private int brow_num;
            private int tag_type;
            private AuthorDTO author;

            public static NewsDTO objectFromData(String str) {

                return new Gson().fromJson(str, NewsDTO.class);
            }

            public static NewsDTO objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), NewsDTO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<NewsDTO> arrayNewsDTOFromData(String str) {

                Type listType = new TypeToken<ArrayList<NewsDTO>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<NewsDTO> arrayNewsDTOFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<NewsDTO>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public List<String> getNews_pics_set() {
                return news_pics_set;
            }

            public void setNews_pics_set(List<String> news_pics_set) {
                this.news_pics_set = news_pics_set;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getLike_num() {
                return like_num;
            }

            public void setLike_num(int like_num) {
                this.like_num = like_num;
            }

            public int getStar_num() {
                return star_num;
            }

            public void setStar_num(int star_num) {
                this.star_num = star_num;
            }

            public int getBb_num() {
                return bb_num;
            }

            public void setBb_num(int bb_num) {
                this.bb_num = bb_num;
            }

            public int getBrow_num() {
                return brow_num;
            }

            public void setBrow_num(int brow_num) {
                this.brow_num = brow_num;
            }

            public int getTag_type() {
                return tag_type;
            }

            public void setTag_type(int tag_type) {
                this.tag_type = tag_type;
            }

            public AuthorDTO getAuthor() {
                return author;
            }

            public void setAuthor(AuthorDTO author) {
                this.author = author;
            }

            public static class AuthorDTO {
                private int id;
                private String username;
                private String password;
                private int gender;
                private String avatar;
                private String create_time;
                private String email;
                private int status;
                private String nickname;
                private String info;
                private int like_num;
                private int star_num;
                private int bb_num;
                private int follow_num;
                private int fans_num;

                public static AuthorDTO objectFromData(String str) {

                    return new Gson().fromJson(str, AuthorDTO.class);
                }

                public static AuthorDTO objectFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);

                        return new Gson().fromJson(jsonObject.getString(str), AuthorDTO.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                public static List<AuthorDTO> arrayAuthorDTOFromData(String str) {

                    Type listType = new TypeToken<ArrayList<AuthorDTO>>() {
                    }.getType();

                    return new Gson().fromJson(str, listType);
                }

                public static List<AuthorDTO> arrayAuthorDTOFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        Type listType = new TypeToken<ArrayList<AuthorDTO>>() {
                        }.getType();

                        return new Gson().fromJson(jsonObject.getString(str), listType);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return new ArrayList();


                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public int getGender() {
                    return gender;
                }

                public void setGender(int gender) {
                    this.gender = gender;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public String getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(String create_time) {
                    this.create_time = create_time;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public int getLike_num() {
                    return like_num;
                }

                public void setLike_num(int like_num) {
                    this.like_num = like_num;
                }

                public int getStar_num() {
                    return star_num;
                }

                public void setStar_num(int star_num) {
                    this.star_num = star_num;
                }

                public int getBb_num() {
                    return bb_num;
                }

                public void setBb_num(int bb_num) {
                    this.bb_num = bb_num;
                }

                public int getFollow_num() {
                    return follow_num;
                }

                public void setFollow_num(int follow_num) {
                    this.follow_num = follow_num;
                }

                public int getFans_num() {
                    return fans_num;
                }

                public void setFans_num(int fans_num) {
                    this.fans_num = fans_num;
                }
            }
        }
    }
}
