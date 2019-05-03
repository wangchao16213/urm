create table computer_view.aaa(
create_timestamp        string,
user_tracking_id        string,                                      
launch_count_tran       string,                                      
launch_event            string,                                      
install_event           string,                                      
prev_session_time       string,                                      
upgrade_event           string,                                      
os_version              string,                                      
resolution              string,                                      
device_name             string,                                      
app_version             string,                                      
download_channel        string,                                      
user_account            string,                                      
btn_query_transaction   string,                                      
charge_products         string,                                      
order_id                string,                                      
pay_type                string,                                      
btn_more                string,                                      
btn_search_keyword      string,                                      
btn_home                string,                                      
mall_events             string,                                      
btn_xiaohao             string,                                      
login_event             string,                                      
page_name               string,                                      
btn_mall                string,                                      
btn_name                string,                                      
btn_position            string,                                      
city                    string,                                      
province                string,                                      
device_id               string,                                      
btn_navigation          string,                                      
btn_information         string,                                      
carrier_name            string,                                      
btn_mall_discount       string,                                      
no_page_name            string,                                      
events                  string,                                      
product_category        string,                                      
product_category_step   string,                                      
current_visit_time      string,                                      
user_visit_id           string,                                      
event                   string,                                      
nbtn_name               string,                                      
nbtn_position           string,                                      
identity                string,                                      
nbtn_bigtype            string,                                      
nbtn_smalltype          string,                                      
rootcity                string,                                      
launchtype              string,
hit_hour                string),
dt                date)
with (format = 'ORC',
partitioned_by = ARRAY['dt']
)














CREATE MATERIALIZED VIEW sc_wap_wx_log_view_tmp as SELECT * FROM sc_wap_wx_log_view;

