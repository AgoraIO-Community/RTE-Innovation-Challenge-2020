<template>
  <div>
    <user-top-menu></user-top-menu>

    <div class="course-container">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>班级信息</span>
        </div>
        <div class="text item">
          班级名称：{{classInfo.class_name}}
        </div>
        <div class="text item">
          班级类型：
          <span v-if="classInfo.type === 1">1v1</span>
          <span v-if="classInfo.type === 2">1v2</span>
          <span v-if="classInfo.type === 3">1v3</span>
        </div>
        <div class="text item">
          班级简介：{{classInfo.description}}
        </div>
      </el-card>

      <el-card class="box-card" style="margin-top: 15px">
        <div slot="header" class="clearfix">
          <span>直播列表</span>
        </div>
        <div>
          <el-table
            :data="liveList"
            style="width: 100%">
            <el-table-column
              prop="class_plan.name"
              label="直播课节">
            </el-table-column>
            <el-table-column
              prop="started_at"
              label="直播时间">
            </el-table-column>
            <el-table-column
              width="180px"
              label="操作">
              <template slot-scope="scope">
                <el-button @click="enterLive(scope.row)" type="text" size="small">进入直播</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>

import { fetchClass, fetchLives } from '../../utils/api'
import UserTopMenu from '../../components/UserTopMenu'

export default {
  name: 'classShow',
  components: { UserTopMenu },
  mounted () {
    this.class_id = this.$route.query.class_id
    this.fetchClass()
    this.fetchLives()
  },
  data () {
    return {
      class_id: 0,
      param: {
        page: 1,
        rows: 10
      },
      classInfo: {},
      liveList: []
    }
  },
  methods: {
    enterLive (row) {
      const query = {
        class_id: row.class_id,
        live_id: row.id
      }
      this.$router.push({
        path: '/live/show',
        query: query
      })
    },
    fetchClass () {
      const that = this
      const param = {
        class_id: this.class_id
      }
      fetchClass(param).then((res) => {
        that.classInfo = res.data.data
      })
    },
    fetchLives () {
      const param = {
        class_id: this.class_id
      }
      const that = this
      fetchLives(param).then((res) => {
        that.liveList = res.data.data
      })
    }
  }
}
</script>

<style>
  .course-container {
    width: 960px;
    margin: 30px auto;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .text {
    font-size: 14px;
    text-align: left;
  }

  .item {
    padding: 18px 0;
  }

  .box-card {
    width: 960px;
  }
</style>
