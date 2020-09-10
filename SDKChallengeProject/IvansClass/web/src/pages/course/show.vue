<template>
  <div>
    <user-top-menu></user-top-menu>

    <div class="course-container">

      <el-container>
        <el-header>{{courseInfo.name}}</el-header>
        <el-main>
          <el-row type="flex" justify="space-around">
            <el-col :span="6" v-for="(item) in classList" v-bind:key="item.id">
              <div class="course-show">
                <div>
                  <el-image
                    style="width: 100%; height: 200px"
                    :src="item.cover_image"
                    fit="cover">
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
                  <el-button type="success" round v-if="item.isJoined" @click="jumpToLive(item.id)">已报名</el-button>
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

import { fetchClasses, fetchCourse, joinClass } from '../../utils/api'
import UserTopMenu from '../../components/UserTopMenu'

export default {
  name: 'courseShow',
  components: { UserTopMenu },
  mounted () {
    this.course_id = this.$route.query.course_id
    this.fetchClasses()
    this.fetchCourse()
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
    fetchCourse () {
      const that = this
      const courseId = this.course_id
      fetchCourse({ course_id: courseId }).then((response) => {
        console.log(response)
        that.courseInfo = response.data.data
      })
    },
    fetchClasses () {
      const that = this
      const param = {
        course_id: this.course_id
      }
      fetchClasses(param).then((res) => {
        that.classList = res.data.data
      })
    },
    joinClass (classId) {
      const param = {
        class_id: classId,
        course_id: this.courseInfo.id
      }
      const that = this
      joinClass(param).then((response) => {
        if (response.data.code === 200) {
          that.$message.info('报名成功')
          that.fetchClasses()
        } else {
          that.$message.error('报名失败')
        }
      })
    },
    jumpToLive (classId) {
      this.$router.push({ path: '/class/show?class_id=' + classId })
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
