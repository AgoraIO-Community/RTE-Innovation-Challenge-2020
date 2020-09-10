import pandas as pd
import json
import math
import time
import matplotlib.pyplot as plt
import datetime
from numba import jit

filename_arr = ["reverse_microwave.tsv", "reverse_hair_dryer.tsv","reverse_pacifier.tsv"]
filename_NLP_arr = ["NLP1_microwave.tsv", "NLP1_hair_dryer.tsv","NLP1_pacifier.tsv"]
time_pin = time.time()
time_start = time.time()
total_finished = 0
total_need = 1615+18939+11470

def return_variables(data):
    marketplace = data.loc[:,'marketplace']
    customer_id = data.loc[:,'customer_id']
    review_id = data.loc[:,'review_id']
    product_id = data.loc[:,'product_id']
    product_parent = data.loc[:,'product_parent']
    product_title = data.loc[:,'product_title']
    product_category = data.loc[:,'product_category']
    star_rating = data.loc[:,'star_rating']
    helpful_votes = data.loc[:,'helpful_votes']
    total_votes = data.loc[:,'total_votes']
    vine = data.loc[:,'vine']
    verified_purchase = data.loc[:,'verified_purchase']
    review_headline = data.loc[:,'review_headline']
    review_body = data.loc[:,'review_body']
    review_date = data.loc[:,'review_date']
    # marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = return_variables(data)

    return marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date


def return_single_variables(data_obj):
        marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = data_obj
        helpful_votes = int(helpful_votes)
        star_rating = int(star_rating)
        total_votes = int(total_votes)
        return marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date

def draw_sentiment_star_charts(attrs):
    # 绘制情绪-评分二维图
    NLP_data,NLP_json_arr,data = attrs
    senti_X = []
    stars_Y = []
    for i in range(len(NLP_data)):
        json_obj = NLP_json_arr[i]
        data_obj = data.values[i]
        sentiment = json_obj["document_sentiment"]["score"]
        senti_X.append(math.floor(sentiment*100)/100)
        stars_Y.append(int(data_obj[7]))
    plt.scatter(senti_X,stars_Y)
    # plt.show()
    
    # 清洗评分二维图数据
    ans = [[senti_X[i],stars_Y[i]] for i in range(len(senti_X))]
    ans = sorted(ans)
    ans_dict = {}
    for i in ans:
        ans_dict["{},{}".format(i[0],i[1])] = ans_dict.get("{},{}".format(i[0],i[1]),{
            "x": i[0],
            "y": i[1],
            "cnt": 0
        })
        ans_dict["{},{}".format(i[0],i[1])]["cnt"] += 1
    ans_arr = []
    for j in ans_dict.items():
        j=j[1]
        ans_arr.append("["+",".join([str(j["x"]),str(j["y"]),str(j["cnt"]*10000000)])+"],")
    print("".join(ans_arr))

def calc_product_stars(attrs):
    # 计算各产品星级
    NLP_data,NLP_json_arr,data = attrs
    item_value = {}
    # stars, sentiment
    for i in range(len(NLP_data)):
        json_obj = NLP_json_arr[i]
        data_obj = data.values[i]
        marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = return_single_variables(data_obj)
        item_value[product_parent] = item_value.get(product_parent,{
            "title": product_title,
            "stars": 0,
            "cnt": 0,
            "avgStars": 0
        })
        item_value[product_parent]["stars"] += star_rating
        item_value[product_parent]["cnt"] += 1
    for j in item_value.items():
        codeA = j[0]
        j = j[1]
        j["avgStars"] = "%.2f" % (j["stars"]/j["cnt"])
        print("代号：%d，均值：%s"%(codeA,j["avgStars"]))

def k_calc_product_reputation(attrs):
     # 计算各产品星级
    NLP_data,NLP_json_arr,data = attrs
    item_value = {}
    # stars, sentiment
    for i in range(len(NLP_data)):
        json_obj = NLP_json_arr[i]
        data_obj = data.values[i]
        marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = return_single_variables(data_obj)

        item_value[product_parent] = item_value.get(product_parent,{
            "title": product_title,
            "stars": 0,
            "cnt": 0,
            "avgStars": 0
        })
        k = 1
        if total_votes != 0:
            if helpful_votes / total_votes > 0.5:
                k = 2 * helpful_votes - total_votes
            else:
                k = 1 / (total_votes - helpful_votes*2)
        
        item_value[product_parent]["stars"] += star_rating * k
        item_value[product_parent]["cnt"] += 1
    for j in item_value.items():
        codeA = j[0]
        j = j[1]
        j["avgStars"] = "%.2f" % (j["stars"]/j["cnt"])
        print("代号：%d，均值：%s"%(codeA,j["avgStars"]))

def get_delta_time(time,days):
    #以当前时间作为起始点，days=-7向前偏移7天，days=7向后偏移7天
    time = time+datetime.timedelta(days=days)
    return time
    
def k_time_calc_product_reputation(attrs, pp):
    # 计算各产品星级
    NLP_data,NLP_json_arr,data = attrs
    item_value = {}
    # stars, sentiment
    for i in range(len(NLP_data)):
        json_obj = NLP_json_arr[i]
        data_obj = data.values[i]
        marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = return_single_variables(data_obj)
        if product_parent != pp:
            continue
        date_standard = "/".join([
            ("0" if len(review_date.split(",")[0].split("/")[i])==1 else "")
        +review_date.split(",")[0].split("/")[i] for i in range(3)])
        # print(date_standard,type(date_standard))
        time_obj = time.strptime(date_standard,"%m/%d/%Y")
        item_value[date_standard] = item_value.get(date_standard,{
            "stars": 0,
            "cnt": 0,
            "reputation": 0,
        })
        k = 1
        if total_votes != 0:
            if helpful_votes / total_votes > 0.5:
                k = helpful_votes / (total_votes - helpful_votes if total_votes - helpful_votes != 0 else 0.5) #* math.ceil(helpful_votes / 100)
                item_value[date_standard]["stars"] += star_rating * k
            else:
                k = (total_votes-helpful_votes) / (helpful_votes if helpful_votes != 0 else 0.5) #* math.ceil((helpful_votes if helpful_votes!=0 else 1) / 100)
                if star_rating > 2.5:
                    print(k,total_votes,helpful_votes,math.ceil(helpful_votes / 100))
                    item_value[date_standard]["stars"] += star_rating / k
                else:
                    item_value[date_standard]["stars"] += star_rating * k
        else:
            item_value[date_standard]["stars"] += star_rating
        item_value[date_standard]["cnt"] += 1
    totalStars = []
    totalCnt = []
    pointer = 0
    time_pointer = None
    current_reputation = 0
    current_cnt = 0

    item_value_original = item_value.copy()
    first_time = None
    last_time = None
    for j in item_value_original.items():
        first_time = j[0] if first_time == None else first_time
        time_pointer = time.strptime(j[0],"%m/%d/%Y")
        time_pointer = datetime.date(time_pointer.tm_year,time_pointer.tm_mon,time_pointer.tm_mday)

        current_reputation = j[1]["stars"]
        current_cnt = j[1]["cnt"]
        for k in range(14):
            new_time = get_delta_time(time_pointer,-k).strftime("%m/%d/%Y")
            item_value[new_time] = item_value.get(new_time,
            {
                "stars": 0,
                "cnt": 0,
                "reputation": 0,
            })
            current_reputation += float(item_value[new_time]["reputation"])
            current_cnt += int(item_value[new_time]["cnt"])
        k = -6
        while current_cnt == j[1]["cnt"]:
            k -= 1
            new_time = get_delta_time(time_pointer,k).strftime("%m/%d/%Y")
            item_value[new_time] = item_value.get(new_time,
            {
                "stars": 0,
                "cnt": 0,
                "reputation": 0,
            })
            current_reputation += float(item_value[new_time]["reputation"])
            current_cnt += int(item_value[new_time]["cnt"])
        j[1]["reputation"] = current_reputation / current_cnt
        # print(current_reputation, current_cnt, j)
        j[1]["reputation"] = "%.2f" % (current_reputation / current_cnt)
        # print("日期：%s，分数：%s"%(j[0],j[1]["reputation"]))
        # print("%s,%s"%(j[0],j[1]["reputation"]))
        last_time = j[0]
    # tmp1 = time.strptime(last_time,"%m/%d/%Y")
    # last_time_pointer = datetime.date(tmp1.tm_year,tmp1.tm_mon,tmp1.tm_mday)
    # tmp1 = time.strptime(first_time,"%m/%d/%Y")
    # first_time_pointer = datetime.date(tmp1.tm_year,tmp1.tm_mon,tmp1.tm_mday)
    # print(last_time_pointer - first_time_pointer < datetime.timedelta(days=0))
    # datetime_zero = datetime.timedelta(days=0)
    # time_pointer = last_time_pointer
    # # while(first_time_pointer - time_pointer > datetime_zero):
    # #     time_pointer = get_delta_time(time_pointer,1)
    lst_time = datetime.date(1900,1,1)
    for i in item_value_original.items():
        tmp1 = time.strptime(i[0],"%m/%d/%Y")
        new_time_obj = datetime.date(tmp1.tm_year,tmp1.tm_mon,tmp1.tm_mday)
        new_time_str = i[0].split("/")[2]+"-"+i[0].split("/")[0]+"-"+i[0].split("/")[1]
        if new_time_obj - lst_time > datetime.timedelta(days=7):
            lst_time = new_time_obj
            print("[\"{}\",{}],".format(new_time_str,i[1]["reputation"]),end="")

def export_low_star_review(attrs,name):
    NLP_data,NLP_json_arr,data = attrs
    ans = ""
    cnt = 0
    for i in range(len(NLP_data)):
        data_obj = data.values[i]
        marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = return_single_variables(data_obj)
        if star_rating < 3:
            ans += review_headline+"\n"+review_body +"\n"
        cnt+=1
        if cnt % 2000 == 1: print(star_rating)
    with open(name+"_LS_review.csv","w",encoding="utf-8") as f:
        f.write(ans)
    print(name)

def export_product_parent(attrs,name):
        # 计算各产品星级
    NLP_data,NLP_json_arr,data = attrs
    products = {}
    # stars, sentiment
    for i in range(len(NLP_data)):
        json_obj = NLP_json_arr[i]
        data_obj = data.values[i]
        marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = return_single_variables(data_obj)
        products[product_parent] = products.get(product_parent,{
            "title": product_title,
        })
    ans_arr = []
    for i in products.items():
        ans_arr.append(str(i[0])+"\t"+i[1]["title"])
    ans = "\n".join(ans_arr)
    with open("./data_analysis_result/{}_product_parent.tsv".format(name),"w",encoding="utf-8") as f:
        f.write(ans)
    return ans

def import_product_parent(name):
    data_pp = pd.read_csv("./data_analysis_result/{}_product_parent.tsv".format(name),sep="\t")
    return data_pp

def get_datetime_by_str(str1):
    try:
        tmp1 = time.strptime(str1,"%m/%d/%Y")
        new_time_obj = datetime.date(tmp1.tm_year,tmp1.tm_mon,tmp1.tm_mday)
        return new_time_obj
    except Exception as identifier:
        print(str1,type(str1),identifier)
        pass

def get_str_by_datetime(date1):
    return date1.strftime("%m/%d/%Y")

def get_2nd_number(num):
    return float("%.2f"%num)

def k_time_calc_product_reputation2(attrs, pp):
    # 计算各产品星级
    NLP_data,NLP_json_arr,data = attrs
    item_value = {}
    first_time = None
    last_time = None

    # 取出所有的pp的值
    for i in range(len(NLP_data)):
        json_obj = NLP_json_arr[i]
        data_obj = data.values[i]
        marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = return_single_variables(data_obj)
        if product_parent != pp:
            continue
        first_time = review_date if type(first_time) != type("123") else first_time
        date_standard = "/".join([
            ("0" if len(review_date.split(",")[0].split("/")[i])==1 else "")
        +review_date.split(",")[0].split("/")[i] for i in range(3)])
        time_obj = time.strptime(date_standard,"%m/%d/%Y")
        item_value[date_standard] = item_value.get(date_standard,{
            "stars": 0,
            "cnt": 0,
            "reputation": 0,
        })

        
        item_value[date_standard]["stars"] += star_rating
        item_value[date_standard]["cnt"] += 1

        last_time = review_date

    
    # 生成从开始到结束的每一天的对象
    item_full_value = {}
    first_time = get_datetime_by_str(first_time)
    last_time = get_datetime_by_str(last_time)
    tmp_time = first_time
    datetime_zero = datetime.timedelta(days=0)
    # 从第一天递归到最后一天
    while (last_time - tmp_time >= datetime_zero):
        # 如果这一天有定义，就直接用定义，如果没有，那加入定义
        item_full_value[get_str_by_datetime(tmp_time)] = \
            item_value.get(get_str_by_datetime(tmp_time),
                {
                    "stars": 0,
                    "cnt": 0,
                    "reputation": 0,
                }
            )
        tmp_time = get_delta_time(tmp_time,1)
    
    time_pointer = None
    current_reputation = 0
    current_cnt = 0

    item_value_original = item_full_value.copy()
    for j in item_value_original.items():
        # 取平均值的长度
        step = 15
        # 取出对应时间
        time_pointer = get_datetime_by_str(j[0])

        current_reputation = j[1]["stars"]*(step/4)
        current_cnt = j[1]["cnt"]*(step/4)
        
        for l in range(1,step+1):
            tmp_time = get_delta_time(time_pointer,-l)
            if first_time - tmp_time > datetime_zero:
                break
            current_reputation += item_value_original[get_str_by_datetime(tmp_time)]["stars"]
            current_cnt += item_value_original[get_str_by_datetime(tmp_time)]["cnt"]
        try:
            j[1]["reputation"] = get_2nd_number(current_reputation / current_cnt)
            j[1]["stars"] = get_2nd_number(current_reputation / current_cnt)
            j[1]["cnt"] = 1
        except Exception as identifier:
            print(identifier,j)
    lst_time = datetime.date(1900,1,1)
    for i in item_value_original.items():
        # print(type(i))
        new_time_obj = get_datetime_by_str(i[0])
        new_time_str = i[0].split("/")[2]+"-"+i[0].split("/")[0]+"-"+i[0].split("/")[1]
        if new_time_obj - lst_time > datetime.timedelta(days=7):
            lst_time = new_time_obj
            print("[\"{}\",{}],".format(new_time_str,i[1]["reputation"]),end="")
            if last_time - new_time_obj < datetime.timedelta(days=7): exit()

def get_star_avg(attrs,pp):
    NLP_data,NLP_json_arr,data = attrs
    item_value = {}
    first_time = None
    last_time = None

    total_stars = 0
    total_cnt = 0
    # 取出所有的pp的值
    for i in range(len(NLP_data)):
        json_obj = NLP_json_arr[i]
        data_obj = data.values[i]
        marketplace,customer_id,review_id,product_id,product_parent,product_title,product_category,star_rating,helpful_votes,total_votes,vine,verified_purchase,review_headline,review_body,review_date = return_single_variables(data_obj)
        if product_parent != pp:
            continue
        total_stars += star_rating
        total_cnt += 1
    return total_stars/total_cnt

def get_all_star_avg(attrs,ppnames):
    NLP_data,NLP_json_arr,data = attrs
    item_value = {}
    first_time = None
    last_time = None
    for i in ppnames:
        item_value[i] = item_value.get(i,{
            "avg": get_star_avg(attrs,i)
        })
    # print("")
    for i in item_value.items():
        print("\"{}\",".format(round(int(i[0])/100000)),end="")
    print("")
    for i in item_value.items():
        print("{},".format(get_2nd_number(i[1]["avg"])),end="")
    
def calc_H(total_votes,helpful_votes,vine,verified_purchase):
    unhelpful_votes = total_votes - helpful_votes
    H = 1+(((helpful_votes - 1.2 * unhelpful_votes)/total_votes) if total_votes != 0 else 0)
    # if H < 0:
    #     return -1
    if vine == "Y": H = H * 1.2
    if verified_purchase == "N": H = H * 0.8
    # if H == 0: print(total_votes,helpful_votes,vine,verified_purchase)
    # print(str(get_2nd_number(H))+",",end="")
    return H

flag = True
def debug_once(varss):
    global flag
    if flag == False: return 0
    flag = False
    print(varss)

def k_time_calc_product_reputation3(attrs, pp):
    # 计算各产品星级
    NLP_data,NLP_json_arr,data = attrs
    item_value = {}
    first_time = None
    last_time = None

    # 取出所有的pp的值

def main():

    limit_ps = [[459626087, 305608994, 930071734, 109226352, 690479711],[670161917, 357308868, 732252283, 486589264, 983445543],[450475749, 572944212, 812583172, 911821018, 246038397]]

    for f in range(2,3):
        # 读入源数据 NLP情绪分析数据
        data = pd.read_csv("./data/"+filename_arr[f],sep="\t")
        NLP_data = pd.read_csv("./data/"+filename_NLP_arr[f],sep="\t").values
        NLP_json_arr = []
        for i in range(len(NLP_data)):
            NLP_json_arr.append(json.loads(NLP_data[i][0]))
        attrs_tuple = (NLP_data,NLP_json_arr,data)
        # 242727854 423421857 459626087
        ppa = 423421857
        # k_time_calc_product_reputation3(attrs_tuple,pp)
        print("")
        # k_time_calc_product_reputation2(attrs_tuple,pp)
        # export_low_star_review(attrs_tuple,filename_arr[f])
        # export_product_parent(attrs_tuple,filename_arr[f])
        # pp_names = import_product_parent(filename_arr[f])
        # make_scatter_time_review2(attrs_tuple,pp_names)
        # get_all_star_avg(attrs_tuple,pp_names.loc[:,"pp"])
        # break
        a = False
        # 输出两者结合的内容
        if a:
            get_star_avg_comment_sentiment2(attrs_tuple,pp_names.loc[:,"pp"].values)
            with open(filename_arr[f]+"ans.json","w") as f:
                for j in range(len(ans_this[1])):
                    print("[{},{}],".format(ans_this[0][j],ans_this[1][j]),end="")
                    f.write("[{},{}],".format(ans_this[0][j],ans_this[1][j]))
                print("")
                f.write("\n")
                for j in range(len(ans_this[1])):
                    print("[{},{}],".format(ans_this[0][j],ans_this[2][j]),end="")
                    f.write("[{},{}],".format(ans_this[0][j],ans_this[2][j]))
                f.write("\n")

        # 输出市场占有率
        # if False:
        #     print("".join(get_market_ratio(attrs_tuple,pp_names)))
        #     limit_pp,limit_pp_name = get_market_ratio(attrs_tuple,pp_names)
        #     print(limit_pp,limit_pp_name)
        #     data123 = calc_product_potential(attrs_tuple,limit_pp)
        #     # with open("new.json","w") as f:
        #     #     f.write(data123)
        #     print(data123)
        #     ans = guiyi(data123,limit_pp_name)
        #     ans.sort(key=lambda x: - x[1])
        #     print(ans[0:5])

        keywords = [["size","space","small","smaller","tiny","quiet","quieter","loud","noisy","price","second","minutes","efficient","power","work","works","worked","function","functions","operate","shipped","ship","install","malfunctions","broke","great","stylish","stainless","beautiful","love","worth","perfect","reliable","warranty","service","Samsung","food","fan","week","plastic","panel","magnetron"],
                    ["size","space","small","smaller","tiny","quiet","quieter","loud","noisy","price","second","minutes","efficient","power","work","works","worked","function","functions","operate","shipped","ship","install","malfunctions","broke","great","stylish","stainless","beautiful","love","worth","perfect","reliable","cord","Conair","heat","warranty","heavy","Dryer","button","bonnet","plastic","speed","light"],
                    ["size","space","small","smaller","tiny","quiet","quieter","loud","noisy","price","second","minutes","efficient","power","work","works","worked","function","functions","operate","shipped","ship","install","malfunctions","broke","great","stylish","stainless","beautiful","love","worth","perfect","reliable","cord","Conair","heat","warranty","heavy","Dryer","button","bonnet","plastic","speed","light","quality","seat","nipple","size","bottles","stroller","waste","pink","newborn","animal"]
                    ]
        calc_key_words_senti(attrs_tuple,keywords[f],limit_ps[f])
        # export_only_top_5(attrs_tuple,filename_arr[f],limit_ps[f])

main()
