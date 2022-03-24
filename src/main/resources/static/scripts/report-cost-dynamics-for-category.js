const data = reportCostDynamicsForCategory;

let title = ['Month'];
const years = [], monthData = [];
let year, date, monthName, outputData = [];

let i;
for (i = 0; i < data.length; i++) {
    year = data[i].year.toString();
    if (!years.includes(year)) {
        years.push(year)
    }
}
title = title.concat(years);

for (let i = 0; i < 12; i++) {
    monthData.push([i]);
    for (let j = 1; j < title.length; j++) {
        monthData[i].push(0);
    }
}

for (let i = 0; i < data.length; i++) {
    monthData[data[i].month - 1][years.indexOf(data[i].year.toString()) + 1] = data[i].totalSum;
}

outputData = [title];

for (let i = 0; i < 12; i++) {
    date = new Date(2020, i, 1);
    monthName = date.toLocaleString('default', {month: 'long'});
    monthData[i][0] = monthName.charAt(0).toUpperCase() + monthName.slice(1)

    outputData.push(monthData[i])
}

//console.log(outputData);

// chart
google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(drawVisualization);

function drawVisualization() {
    // Some raw data (not necessarily accurate)
    var data = google.visualization.arrayToDataTable(outputData);

    var options = {
        title: 'Динаміка витрат в році (по роках)',
        titleTextStyle: {
            // color: <string>,
            fontName: 'Segoe UI',
            fontSize: 24,
            bold: true,
            italic: false
        },
        vAxis: {
            title: 'Сума',
            titleTextStyle: {
                // color: <string>,
                fontName: 'Segoe UI',
                fontSize: 16,
                bold: true,
                italic: false
            }
        },
        hAxis: {
            title: 'Місяць',
            titleTextStyle: {
                // color: <string>,
                fontName: 'Segoe UI',
                fontSize: 16,
                bold: true,
                italic: false
            }
        },
        seriesType: 'bars',
        series: {5: {type: 'line'}}
    };

    var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
    chart.draw(data, options);
}
