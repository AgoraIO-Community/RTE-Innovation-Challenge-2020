<template>
  <div>
    <user-menu></user-menu>

    <div class="course-container">
    <el-container>
      <el-header>我参加的课程</el-header>
      <el-main>
        <el-row type="flex" justify="space-around">
          <el-col :span="6" v-for="(item) in courseList" v-bind:key="item.id">
            <div class="course-show" @click="jumpToShow(item.course_id)">
              <div>
                <el-image
                  style="width: 100%; height: 200px"
                  :src="item.course.cover_image"
                  fit="cover">
                </el-image>
              </div>
              <p class="course-name">
                {{item.course.class_name}}
              </p>
              <p class="class-desc">
                {{item.course.description}}
              </p>
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
  </div>
</template>

<script>

import { fetchUserCourses } from '../../utils/api'
import UserMenu from '../../components/UserMenu'

export default {
  name: 'UserClass',
  components: { UserMenu },
  mounted () {
    this.fetchUserCourses()
  },
  data () {
    return {
      course_id: 0,
      param: {
        page: 1,
        rows: 10
      },
      courseList: []
    }
  },
  methods: {
    jumpToShow (id) {
      this.$router.push({
        path: '/courses/show?course_id=' + id
      })
    },
    fetchUserCourses () {
      const that = this
      const param = {}
      fetchUserCourses(param).then((res) => {
        that.courseList = res.data.data
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
