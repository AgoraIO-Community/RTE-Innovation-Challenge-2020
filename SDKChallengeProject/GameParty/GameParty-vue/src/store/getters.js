const getters = {
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  phone: state => state.user.phone,
  gameTypeList: state => state.game.gameTypeList,
  activeAreaId: state => state.game.activeAreaId
}
export default getters
