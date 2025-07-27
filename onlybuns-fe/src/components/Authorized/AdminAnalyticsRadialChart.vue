<template>
    <div class="chart-container">
      <div class="chart-wrapper">
        <DoughnutChart
          :chart-data="chartData"
          :chart-options="chartOptions"
        />
      </div>
      <br>
      <br>
      <div class="stats-grid">
        <div 
          v-for="(stat, index) in formattedLabels" 
          :key="index"
          class="stat-card"
          :style="{ borderColor: chartColors[index] }"
        >
          <h3>{{ stat.label }}</h3>
          <div class="stat-numbers">
            <div class="user-count">{{ stat.count }}</div>
            <div class="percentage">{{ stat.percentage }}%</div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { defineComponent } from 'vue';
  import { DoughnutChart } from 'vue-chart-3';
  import { Chart, registerables } from 'chart.js';
  
  Chart.register(...registerables);
  
  export default defineComponent({
    name: 'RadialChart',
    components: { DoughnutChart },
    props: {
      usersJustCommented: {
        type: Number,
        required: true,
      },
      usersJustPosted: {
        type: Number,
        required: true,
      },
      usersWithoutPostsOrComments: {
        type: Number,
        required: true,
      },
    },
    data() {
      return {
        chartColors: ['#4CAF50', '#2196F3', '#FFC107']
      };
    },
    computed: {
      totalUsers() {
        return this.usersJustCommented + 
               this.usersJustPosted + 
               this.usersWithoutPostsOrComments;
      },
      formattedLabels() {
        return [
          {
            count: this.usersJustCommented,
            label: 'Only Commented',
            percentage: (this.usersJustCommented / this.totalUsers * 100).toFixed(1)
          },
          {
            count: this.usersJustPosted,
            label: 'Posted',
            percentage: (this.usersJustPosted / this.totalUsers * 100).toFixed(1)
          },
          {
            count: this.usersWithoutPostsOrComments,
            label: 'Neither',
            percentage: (this.usersWithoutPostsOrComments / this.totalUsers * 100).toFixed(1)
          }
        ];
      },
      chartData() {
        return {
          labels: this.formattedLabels.map(item => item.label),
          datasets: [
            {
              data: [
                this.usersJustCommented,
                this.usersJustPosted,
                this.usersWithoutPostsOrComments,
              ],
              backgroundColor: this.chartColors,
              borderWidth: 2,
              borderColor: '#ffffff',
              hoverBorderWidth: 4,
            },
          ],
        };
      },
      chartOptions() {
        return {
          responsive: true,
          maintainAspectRatio: false,
          cutout: '65%',
          plugins: {
            legend: {
              display: false
            },
            tooltip: {
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              titleColor: '#333',
              bodyColor: '#666',
              borderColor: '#ddd',
              borderWidth: 1,
              padding: 12,
              cornerRadius: 8,
              callbacks: {
                label: (context) => {
                  const value = context.raw;
                  const percentage = ((value / this.totalUsers) * 100).toFixed(1);
                  return `${value.toLocaleString()} users (${percentage}%)`;
                }
              }
            }
          },
          animation: {
            animateScale: true,
            animateRotate: true,
            duration: 2000,
            easing: 'easeInOutQuart'
          }
        };
      },
    },
  });
  </script>
  
  <style scoped>
  .chart-container {
    padding: 2rem;
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .chart-wrapper {
    height: 300px;
    margin-bottom: 2rem;
  }
  
  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
    margin-top: 2rem;
  }
  
  .stat-card {
    padding: 1rem;
    border-radius: 8px;
    border-left: 4px solid;
    background: #f8f9fa;
    transition: transform 0.2s ease;
  }
  
  .stat-card:hover {
    transform: translateY(-2px);
  }
  
  .stat-card h3 {
    margin: 0 0 0.5rem 0;
    font-size: 1rem;
    color: #333;
  }
  
  .stat-numbers {
    display: flex;
    justify-content: space-between;
    align-items: baseline;
  }
  
  .user-count {
    font-size: 1.5rem;
    font-weight: bold;
    color: #2c3e50;
  }
  
  .percentage {
    font-size: 1.1rem;
    color: #666;
  }
  </style>