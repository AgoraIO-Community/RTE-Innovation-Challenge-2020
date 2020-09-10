<template>
  <div>
    <user-top-menu></user-top-menu>

    <div class="course-container">
    <el-container>
      <el-header>课程列表</el-header>
      <el-main>
        <el-row type="flex" justify="space-around">
          <el-col :span="6" v-for="(item) in courseList" v-bind:key="item">
            <div class="course-show" @click="jumpToShow(item.id)">
              <div>
                <el-image
                  style="width: 100%; height: 200px"
                  :src="item.cover_image"
                  :fit="cover">
                </el-image>
              </div>
              <p class="course-name">
                {{item.name}}
              </p>
              <p></p>
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
  </div>
</template>

<script>
import { fetchCourses } from '../../utils/api'
import UserTopMenu from '../../components/UserTopMenu'

export default {
  name: 'fetchCourses',
  components: { UserTopMenu },
  mounted () {
    this.fetchCourses()
  },
  data () {
    return {
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
    fetchCourses () {
      const that = this
      fetchCourses(this.param).then((res) => {
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
