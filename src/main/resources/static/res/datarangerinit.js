function datarangerinit(jqDateRangerBtn,jqDateRangerSpan) {
    var dateranger = null;
    //定义locale汉化插件
    var locale = {
        "format": 'YYYY-MM-DD',
        "separator": " -222 ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "起始时间",
        "toLabel": "结束时间'",
        "customRangeLabel": "自定义",
        "weekLabel": "W",
        "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
        "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        "firstDay": 1
    };
    //初始化显示当前时间
    jqDateRangerSpan.html(moment().subtract('hours', 1).format('YYYY-MM-DD') + ' - ' + moment().format(
        'YYYY-MM-DD'));
    dateranger = moment().format('YYYY-MM-DD') + ',' + moment().format('YYYY-MM-DD');
    //日期控件初始化
    jqDateRangerBtn.daterangepicker({
            'locale': locale,
            //汉化按钮部分
            ranges: {
                '今日': [moment(), moment()],
                '昨日': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                '最近7日': [moment().subtract(6, 'days'), moment()],
                '最近30日': [moment().subtract(29, 'days'), moment()],
                '本月': [moment().startOf('month'), moment().endOf('month')],
                '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf(
                    'month')]
            },
            startDate: moment().subtract(29, 'days'),
            endDate: moment()
        },
        function (start, end) {
            jqDateRangerSpan.html(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
        }
    );
    jqDateRangerBtn.on('apply.daterangepicker', function (ev, picker) {
        switch (picker.chosenLabel) {
            case '今日':
                {
                    autodate_type = '0';
                    break;
                }
            case '昨日':
                {
                    autodate_type = '1';
                    break;
                }
            case '最近7日':
                {
                    autodate_type = '2';
                    break;
                }
            case '最近30日':
                {
                    autodate_type = '3';
                    break;
                }
            case '本月':
                {
                    autodate_type = '4';
                    break;
                }
            case '上月':
                {
                    autodate_type = '5';
                    break;
                }
            case '自定义':
                {
                    autodate_type = '6';
                    break;
                }
        }
        jqDateRangerBtn.html(picker.chosenLabel + '|' + picker.startDate.format('YYYY-MM-DD') +
            ' - ' + picker.endDate.format('YYYY-MM-DD'));
        dateranger = picker.startDate.format('YYYY-MM-DD') + ',' + picker.endDate.format('YYYY-MM-DD');
    });
    return dateranger;
}