<template>
  <div>
    <user-menu></user-menu>

    <div class="course-container">
    <el-container>
      <el-header>我加入的班级</el-header>
      <el-main>
        <el-row type="flex" justify="space-around">
          <el-col :span="6" v-for="(item) in classList" v-bind:key="item">
            <div class="course-show">
              <div>
                <el-image
                  style="width: 100%; height: 200px"
                  :src="item.cover_image"
                  :fit="cover">
                </el-image>
              </div>
              <p class="course-name">
                {{item.class_name}}
              </p>
              <p class="class-desc">
                {{item.description}}
              </p>
              <div>
                <el-button type="primary" round v-if="!item.isJoined" @click="joinClass(item.id)">立即报名</el-button>
                <el-button type="success" round v-if="item.isJoined">已报名</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
  </div>
</template>

<script>

import { fetchUserLives } from '../../utils/api'
import UserMenu from '../../components/UserMenu'

export default {
  name: 'UserClass',
  components: { UserMenu },
  mounted () {
    this.fetchUserLives()
  },
  data () {
    return {
      course_id: 0,
      param: {
        page: 1,
        rows: 10
      },
      classList: [],
      courseInfo: {}
    }
  },
  methods: {
    fetchUserLives () {
      const that = this
      const param = {}
      fetchUserLives(param).then((res) => {
        that.classList = res.data.data
      })
    }
  }
}
</script>

<style>
  .course-container {
    margin: 30px auto;
  }

  .course-name {
    font-weight: bold;
  }

  .course-show {
    border: 1px solid #eee;
    padding: 15px;
    box-shadow: 0 0 5px #909399;
  }

  .course-show:hover {
    box-shadow: 0 0 25px #909399;

  }
</style>
