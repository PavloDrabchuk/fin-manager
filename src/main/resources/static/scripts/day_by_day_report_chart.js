google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    // const reportDayByDay = /*[[${reportDayByDay}]]*/ null;

    const dataReport = []
    for (var i = 0; i < reportDayByDay.length; i++) {
        dataReport.push([new Date(reportDayByDay[i].date), reportDayByDay[i].totalSum])
    }

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Дата');
    data.addColumn('number', 'Сума');

    data.addRows(dataReport);

    var formatter_short = new google.visualization.DateFormat({pattern: "dd.MM.yyyy"});

    // Reformat our data.
    formatter_short.format(data, 0);

    var options = {
        title: 'Розподіл витрат за період по датах',
        titleTextStyle: {
            // color: <string>,
            fontName: 'Segoe UI',
            fontSize: 24,
            bold: true,
            italic: false
        },
        width: 900,
        height: 500,
        hAxis: {
            format: 'dd.MM.yyyy',
            gridlines: {count: 15},
            title: 'Дата',
            titleTextStyle: {
                // color: <string>,
                fontName: 'Segoe UI',
                fontSize: 16,
                bold: true,
                italic: false
            }
        },
        vAxis: {
            minValue: 0,
            title: 'Сума',
            titleTextStyle: {
                // color: <string>,
                fontName: 'Segoe UI',
                fontSize: 16,
                bold: true,
                italic: false
            }
        },
        pointSize: 4,
        legend: {
            textStyle: {
                // color: <string>,
                fontName: 'Segoe UI',
                fontSize: 16,
                bold: true,
                italic: false
            }
        }
    };

    var chart = new google.visualization.LineChart(document.getElementById('day_by_day_report_chart'));

    chart.draw(data, options);
}