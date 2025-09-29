'use client'

import style from "./page.module.css";
import { IoEye } from "react-icons/io5";
import { IoEyeOff } from "react-icons/io5";
import { useState } from "react";
import { CredentialsDTO } from "./models/auth";
import * as authService from "./services/auth-service";
import { useRouter } from "next/navigation";

export default function Home() {

  const router = useRouter();

  const [formData, setFormData] = useState<CredentialsDTO>({
    username: '',
    password: ''
  })

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value })
  }

  const [showPassword, setShowPassword] = useState(false);

  function handleSubmit(event: React.FormEvent<EventTarget>) {
    event.preventDefault()
    authService.loginRequest(formData)
      .then(response => {
        authService.saveAccessToken(response.data.access_token)
        router.push('/workouts')
      })
      .catch(error => {
        console.log('Erro:', error.response.data)
      })
  }

  return (
      <div className={style.container}>

        <form className={style.formContainer} onSubmit={handleSubmit}>
          <input className={style.form}
            name="username"
            type="email"
            placeholder="Email"
            value={formData.username}
            onChange={handleInputChange}
          />

          <input className={style.form}
            name="password"
            type={showPassword ? "text" : "password"}
            placeholder="Password"
            value={formData.password}
            onChange={handleInputChange}
            />

            {showPassword ? (
              <IoEye
                className={style.eye}
                onClick={() => setShowPassword(false)}
              />
            ) : (
              <IoEyeOff
                className={style.eye}
                onClick={() => setShowPassword(true)}
              />
            )}
            
          <button className={style.loginButton} type="submit">login</button>
          <span className={style.signUp} onClick={() => router.push('/signup')}>Não possui conta? Clique aqui</span>
        </form>
      </div>
  );
}
